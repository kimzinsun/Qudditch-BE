package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.dto.StoreStock;
import com.goldensnitch.qudditch.dto.StoreStockReport;
import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.dto.payment.PaymentResponse;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    // DB에 주문 정보와 주문 상품 정보를 저장하기 위한 매퍼
    @Autowired
    private CustomerOrderProductMapper customerOrderProductMapper;

    @Autowired
    private StoreStockMapper storeStockMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartService cartService;

    @Transactional
    public String createOrder(Integer userCustomerId) {
        // 사용자의 장바구니 아이템 조회
        List<CartItem> cartItems = cartService.getCartItem(userCustomerId);

        if (cartItems.isEmpty()) {
            return "장바구니가 비어 있습니다.";
        }

//        List<Product> products = productMapper.findProductsByUserCustomerId(userCustomerId);
//        if (products.isEmpty()) {
//            return "상품 정보를 찾을 수 없습니다.";
//        }

        // cartItem 에서 첫 번째 상품의 이름을 사용하도록 수정
        String itemName = cartItems.get(0).getProductName();

        // 결제 승인된 상태를 가정 (실제 결제 승인 로직은 별도로 처리)
        int totalAmount = calculateTotalAmount(cartItems);

        // 결제 페이지 URL을 받아오는 부분
        String paymentPageUrl = paymentService.initiatePayment(
                "TC0ONETIME",
                userCustomerId.toString(),
                "partner_user_id",
                itemName,
                1, // 수량, 실제 수량으로 교체 필요
                totalAmount,
                0,
                "http://localhost:8080/approval", // 승인 URL
                "http://localhost:8080/cancel", // 취소 URL
                "http://localhost:8080/fail" // 실패 URL
        );

        return "결제 페이지 URL: " + paymentPageUrl;
    }

    // 결제 승인을 위한 메서드, 실제 구현에서는 사용자로부터 받은 pgToken 등을 활용
    public String approvePayment(String pgToken, String tid, Integer userCustomerId) {
        PaymentResponse paymentResponse = paymentService.approvePayment(tid, pgToken);
        if (paymentResponse == null) {
            throw new RuntimeException("결제 승인 실패");
        }

        // 사용자의 장바구니 아이템 조회
        List<CartItem> cartItems = cartService.getCartItem(userCustomerId);
        if (cartItems.isEmpty()) {
            return "장바구니가 비어 있습니다.";
        }

        // 장바구니 아이템을 기반으로 총 결제 금액 계산
        int totalAmount = calculateTotalAmount(cartItems);

        // 장바구니 아이템 중 첫 번째 항목의 userStoreId를 사용하여 모든 주문 아이템이 같은 매장에서 추가되었음을 가정
        Integer userStoreId = cartItems.get(0).getUserStoreId();

        // 고객 오더 정보 생성 및 저장
        CustomerOrder customerOrder = setupCustomerOrder(userCustomerId, totalAmount, userStoreId);

        // 각 상품에 대한 주문 상품 정보 생성 및 저장
        List<CustomerOrderProduct> orderProducts = setupCustomerOrderProducts(customerOrder.getId(), cartItems);

        // 장바구니 비우기
        cartService.clearCart(userCustomerId);

        // 재고 업데이트(재고 차감)
        decrementStockAndUpdateReport(cartItems, orderProducts, customerOrder.getUserStoreId());

        return "주문이 성공적으로 처리되었습니다. 주문 번호: " + customerOrder.getId();
    }

    private int calculateTotalAmount(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(item -> item.getProductPrice() * item.getQty())
                .sum();
    }

    private CustomerOrder setupCustomerOrder(Integer userCustomerId, int totalAmount, Integer userStoreId){
        CustomerOrder order = new CustomerOrder();
        order.setUserCustomerId(userCustomerId);
        order.setTotalAmount(totalAmount);
        order.setUserStoreId(userStoreId); // 매장 정보 설정
        order.setUsedPoint(0); // used_point에 대한 기본값 설정
        order.setTotalPay(totalAmount); // 예시로 totalAmount 값을 사용, 실제 비즈니스 로직에 맞게 조정 필요
        order.setEarnPoint(0); // earn_point에 대한 기본값 설정
        // order 객체를 데이터베이스에 삽입
        customerOrderProductMapper.insertCustomerOrder(order); // 수정된 부분
        return order;
    }

    private List<CustomerOrderProduct> setupCustomerOrderProducts(Integer orderId, List<CartItem> cartItems){
        List<CustomerOrderProduct> customerOrderProducts = new ArrayList<>();
        cartItems.forEach(item -> {
            CustomerOrderProduct product = new CustomerOrderProduct();
            product.setCustomerOrderId(orderId);
            product.setProductId(item.getProductId());
            product.setQty(item.getQty());
            // customerOrderProduct 객체를 데이터베이스에 삽입
            customerOrderProductMapper.insertCustomerOrderProduct(product); // 수정된 부분
            customerOrderProducts.add(product); // 리스트에 추가
        });
        return customerOrderProducts;
    }

    private void decrementStockAndUpdateReport(List<CartItem> cartItems, List<CustomerOrderProduct> orderProducts, Integer userStoreId) {
        orderProducts.forEach(orderProduct -> {
            // CartItem 대신 CustomerOrderProduct 사용하여 재고 차감 로직 수정
            StoreStock currentStock = storeStockMapper.selectProductByUserStoreIdAndProductId(userStoreId, orderProduct.getProductId());
            if (currentStock != null && currentStock.getQty() >= orderProduct.getQty()) {
                // 재고 차감 (현재 재고 - 주문 수량)
                int newQty = currentStock.getQty() - orderProduct.getQty();
                storeStockMapper.updateStockQty(userStoreId, orderProduct.getProductId(), newQty);

                // 재고 로그 기록 (StoreStockReport와 관련된 mapper 메서드 필요)
                // StoreStockReport 객체 생성 및 필요한 값 설정
                StoreStockReport stockReport = new StoreStockReport();
                stockReport.setUserStoreId(currentStock.getUserStoreId());
                stockReport.setProductId(orderProduct.getProductId());
                stockReport.setYmd(java.sql.Date.valueOf(LocalDate.now())); // 현재 날짜 설정
                stockReport.setOutQty(orderProduct.getQty());
                // 재고 로그 기록을 위한 mapper 메서드 호출
                storeStockMapper.insertStoreStockReport(stockReport);
            } else {
                // 재고 부족 예외 처리
                throw new RuntimeException("재고가 부족합니다. 제품 ID: " + orderProduct.getProductId());
            }
        });
    }
}


/*
    # 코드 개선 사항(24-03-16)
    기능을 크게 3가지로 분류
    1. 결제 (요청-승인-취소)
    2. 고객 오더 생성(DB): 결제 승인된 품목은 customerOrder, customerOrderProduct DB로 저장
    3. DB 저장된 품목 가게 재고에서 차감, 판매 로그 기록


    # 1. 결제 (요청-승인-취소)
    문제: 물건 구매 시에 cartItem 객체를 paymentRequest 객체로 변환하는 과정에서 값이 제대로 전달이 되는지 확인 필요
    해결
      1) 카트 아이템 정보 결제 요청 데이터로 변환
      2) 결제 요청 데이터 로직 개선
      3) 변환된 결제 요청 데이터 검증
 */
package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    // 상품 정보 조회를 위한 매퍼
    @Autowired
    private ProductMapper productMapper;

    // DB에 주문 정보와 주문 상품 정보를 저장하기 위한 매퍼
    @Autowired
    private CustomerOrderProductMapper customerOrderProductMapper;

    @Autowired
    private StoreStockMapper storeStockMapper;

    @Autowired
    private ProductService productService; // 상품 서비스 의존성 추가

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartService cartService;

    @Transactional
    public String createOrder(List<CartItem> cartItems, Integer userCustomerId) {
        if (cartItems.isEmpty()) {
            return "장바구니가 비어 있습니다.";
        }

        // 결제 승인된 상태를 가정 (실제 결제 승인 로직은 별도로 처리)
        int totalAmount = calculateTotalAmount(cartItems);

        // 고객 오더 정보 생성 및 저장
        CustomerOrder customerOrder = setupCustomerOrder(userCustomerId, totalAmount);

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
                .mapToInt(item -> {
                    Product product = productMapper.findByProductId(item.getProductId());
                    return product.getPrice() * item.getQty();
                })
                .sum();
    }

    private CustomerOrder setupCustomerOrder(Integer userCustomerId, int totalAmount){
        CustomerOrder order = new CustomerOrder();
        order.setUserCustomerId(userCustomerId);
        order.setTotalAmount(totalAmount);
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
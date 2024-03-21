package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Integer createOrder(Integer userCustomerId, List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            throw new RuntimeException("장바구니가 비어 있습니다.");
        }

        // 주문 생성
        Integer totalAmount = calculateTotalAmount(cartItems);
        CustomerOrder order = new CustomerOrder();
        setupCustomerOrder(userCustomerId, totalAmount);
        setupCustomerOrderProducts(order.getId(), cartItems);

        // 아래 결제 페이지 URL 요청 로직은 삭제하고, 대신 주문 ID를 반환
        return order.getId(); // 생성된 주문의 ID 반환
    }

    // 결제 승인을 위한 메서드, 실제 구현에서는 사용자로부터 받은 pgToken 등을 활용
//    public String approvePayment(String pgToken, Integer orderId) {
//        CustomerOrder order = customerOrderProductMapper.findById(orderId);
//        if (order == null) {
//            throw new RuntimeException("Order not found.");
//        }
//
//        // String 대신 PaymentResponse를 반환하도록 변경된 PaymentService의 approvePayment 메서드 호출
//        String paymentResult = paymentService.approvePayment(pgToken, orderId);
//
//        // PaymentService에서 "Payment approved successfully." 문자열을 반환하면 성공으로 간주
//        if (!"Payment approved successfully.".equals(paymentResult)) {
//            throw new RuntimeException("결제 승인 실패");
//        }
//
//        updateOrderStatus(orderId, order.getTid()); // 주문 상태 업데이트 로직 추가
//
//        // 주문에 포함된 상품 정보 조회
//        List<CustomerOrderProduct> orderProducts = customerOrderProductMapper.findByOrderId(orderId);
//
//        // 재고 업데이트(재고 차감)
//        decrementStockAndUpdateReport(order.getUserStoreId(), orderProducts);
//
//        return "주문이 성공적으로 처리되었습니다. 주문 번호: " + order.getId();
//    }

    private int calculateTotalAmount(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(item -> item.getProductPrice() * item.getQty())
                .sum();
    }

    private void setupCustomerOrder(Integer userCustomerId, Integer totalAmount){
        CustomerOrder order = new CustomerOrder();
        order.setUserCustomerId(userCustomerId);
        order.setTotalAmount(totalAmount);
        order.setUsedPoint(0); // used_point에 대한 기본값 설정
        order.setTotalPay(totalAmount); // 예시로 totalAmount 값을 사용, 실제 비즈니스 로직에 맞게 조정 필요
        order.setEarnPoint(0); // earn_point에 대한 기본값 설정
        // order 객체를 데이터베이스에 삽입
        customerOrderProductMapper.insertCustomerOrder(order); // 수정된 부분
    }

    private void setupCustomerOrderProducts(Integer orderId, List<CartItem> cartItems){
        cartItems.forEach(item -> {
            CustomerOrderProduct product = new CustomerOrderProduct();
            product.setCustomerOrderId(orderId);
            product.setProductId(item.getProductId());
            product.setQty(item.getQty());
            // customerOrderProduct 객체를 데이터베이스에 삽입
            customerOrderProductMapper.insertCustomerOrderProduct(product); // 수정된 부분
        });
    }

//    private void updateOrderStatus(Integer orderId, String tid) {
//        // 주문 상태 업데이트 또는 결제 고유번호(tid) 저장 로직 구현
//        CustomerOrder order = customerOrderProductMapper.findById(orderId);
//        if (order != null) {
//            order.setTid(tid);
//            customerOrderProductMapper.update(order); // 주문 정보 업데이트
//        }
//    }

//    private void decrementStockAndUpdateReport(Integer userStoreId, List<CustomerOrderProduct> orderProducts) {
//        orderProducts.forEach(orderProduct -> {
//            // CartItem 대신 CustomerOrderProduct 사용하여 재고 차감 로직 수정
//            StoreStock currentStock = storeStockMapper.selectProductByUserStoreIdAndProductId(userStoreId, orderProduct.getProductId());
//            if (currentStock != null && currentStock.getQty() >= orderProduct.getQty()) {
//                // 재고 차감 (현재 재고 - 주문 수량)
//                int newQty = currentStock.getQty() - orderProduct.getQty();
//                storeStockMapper.updateStockQty(userStoreId, orderProduct.getProductId(), newQty);
//
//                // 재고 로그 기록 (StoreStockReport와 관련된 mapper 메서드 필요)
//                // StoreStockReport 객체 생성 및 필요한 값 설정
//                StoreStockReport stockReport = new StoreStockReport();
//                stockReport.setUserStoreId(currentStock.getUserStoreId());
//                stockReport.setProductId(orderProduct.getProductId());
//                stockReport.setYmd(java.sql.Date.valueOf(LocalDate.now())); // 현재 날짜 설정
//                stockReport.setOutQty(orderProduct.getQty());
//                // 재고 로그 기록을 위한 mapper 메서드 호출
//                storeStockMapper.insertStoreStockReport(stockReport);
//            } else {
//                // 재고 부족 예외 처리
//                throw new RuntimeException("재고가 부족합니다. 제품 ID: " + orderProduct.getProductId());
//            }
//        });
//    }
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
package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.payment.CartItem;
import com.goldensnitch.qudditch.dto.payment.PaymentRequest;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private PaymentService paymentService;

    @Autowired
    private CartService cartService;

    @Autowired
    private StoreStockMapper storeStockMapper;

    @Transactional
    public String createOrder(List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            return "장바구니가 비어 있습니다.";
        }

        // 총액 계산
        int totalAmount = calculateTotalAmount(cartItems);

        // 장바구니에서 첫 번째 항목의 userStoreId를 사용하여 모든 항목이 같은 매장에서 추가되었는지 가정
        Integer userStoreId = cartItems.get(0).getUserStoreId();

        // userCustomerId는 cartItems의 첫 번째 요소에서 추출
        Integer userCustomerId = cartItems.get(0).getUserCustomerId();

//        // 고객 주문 설정 (DB x)
//        List<CustomerOrderProduct> orderProduct = setupCustomerOrderProduct(cartItems, customerOrder.getId());

        // 실제 결제에 필요한 정보 설정(결제 요청 -> PaymentService)
        PaymentRequest paymentRequest = createPaymentRequest(cartItems);

        // 결제 처리 및 리다이렉트 URL 반환
        String paymentRedirectUrl = paymentService.initiatePayment("TC0ONETIME", "partner_order_id_example",
                "partner_user_id_example", "test item", 1, 10000, 0,
                "http://localhost:8080/approval", "http://localhost:8080/cancel", "http://localhost:8080/fail");

        if ("Error".equals(paymentRedirectUrl)) {
            return "결제 처리 중 오류가 발생했습니다.";
        }

//        // 결제 성공 품목을 DB에 저장(customer)
//        customerOrderProductMapper.insertCustomerOrder(customerOrder);
//        orderProduct.forEach(customerOrderProductMapper::insertCustomerOrderProduct);

        // 장바구니 비우기
        cartService.clearCart(userCustomerId);

//        // 결제 성공 품목의 재고 차감
//        decrementStockAndUpdateReport(cartItems, orderProduct);

        return paymentRedirectUrl;
    }

    private int calculateTotalAmount(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(item -> {
                    Product product = productMapper.findByProductId(item.getProductId());
                    return product.getPrice() * item.getQty();
                })
                .sum();
    }

//    private CustomerOrder setupCustomerOrder(Integer userCustomerId, int totalAmount, Integer userStoreId){
//        CustomerOrder customerOrder = new CustomerOrder();
//        customerOrder.setUserCustomerId(userCustomerId);
//        customerOrder.setTotalAmount(totalAmount);
//        customerOrder.setUsedPoint(0); // 사용한 포인트 기본값 설정
//        customerOrder.setTotalPay(totalAmount); // 총 결제액은 총액과 동일하게 초기 설정
//        customerOrder.setEarnPoint(0); // 획득 포인트 기본값 설정
//        customerOrder.setUserStoreId(userStoreId); // userStoreId 설정
//        customerOrder.setOrderedAt(java.sql.Date.valueOf(LocalDate.now())); // LocalDate 사용
//        customerOrder.setTid("임시_TID_값");
//
//        customerOrderProductMapper.insertCustomerOrder(customerOrder);
//        return customerOrder;
//    }

//    private List<CustomerOrderProduct> setupCustomerOrderProduct(List<CartItem> cartItems, Integer orderId){
//        List<CustomerOrderProduct> customerOrderProduct = new ArrayList<>();
//        cartItems.forEach(item -> {
//            CustomerOrderProduct product = new CustomerOrderProduct();
//            product.setCustomerOrderId(orderId);
//            product.setProductId(item.getProductId());
//            product.setQty(item.getQty());
//            customerOrderProduct.add(product);
//        });
//        return customerOrderProduct;
//    }

    private PaymentRequest createPaymentRequest(List<CartItem> cartItems) {
        PaymentRequest paymentRequest = new PaymentRequest();

        // userCustomerId는 cartItems의 첫 번째 요소에서 추출
        Integer userCustomerId = cartItems.get(0).getUserCustomerId();

        // 결제 요청에 필요한 정보 설정
        if (!cartItems.isEmpty()) {
            Product firstProduct = productMapper.findByProductId(cartItems.get(0).getProductId());
            paymentRequest.setItem_name(firstProduct.getName());
            int totalQuantity = cartItems.stream().mapToInt(CartItem::getQty).sum();
            paymentRequest.setQuantity(totalQuantity);
        }

        // 사용 포인트, 총 결제 금액, 획득 포인트 계산 (여기서는 예시 값으로 설정)
        int totalAmount = calculateTotalAmount(cartItems);
        paymentRequest.setTotal_amount(totalAmount);
        int usedPoint = calculateUsedPoints(userCustomerId); // 사용 포인트 계산 함수
        int totalPay = totalAmount - usedPoint; // 총 결제 금액 = 총액 - 사용 포인트
        int earnPoint = calculateEarnPoints(totalPay); // 획득 포인트 계산 함수

        paymentRequest.setTotal_amount(totalAmount);
        paymentRequest.setUsedPoint(usedPoint);
        paymentRequest.setTotalPay(totalPay);
        paymentRequest.setEarnPoint(earnPoint);

        // 비과세 null 체크: null값인 경우 0원으로 설정
        Integer taxFreeAmount = paymentRequest.getTax_free_amount();
        paymentRequest.setTax_free_amount(taxFreeAmount != null ? taxFreeAmount : 0); // 수정된 부분

        // CID, 주문 ID 등 설정
        // paymentRequest.setCid("TC0ONETIME"); // 예시 CID, 실제 환경에서는 설정 필요
        paymentRequest.setCid("04CF21E2C19A63303B01"); // 카카오페이로부터 받은 가맹점 코드 설정
        // 주문 ID, 사용자 ID 등의 정보 설정
        paymentRequest.setPartner_order_id("OrderID_" + System.currentTimeMillis()); // 예시로 현재 시간을 기반으로 한 주문 ID 생성
        paymentRequest.setPartner_user_id(userCustomerId.toString());

        // Redirect URL 설정 (실제 운영 환경에 맞게 수정)
        paymentRequest.setApproval_url("http://localhost:8080/approval");
        paymentRequest.setCancel_url("http://localhost:8080/cancel");
        paymentRequest.setFail_url("http://localhost:8080/fail");

        return paymentRequest;
    }

    // 사용 포인트 계산 예시 함수
    private int calculateUsedPoints(Integer userCustomerId) {
        // 사용자 ID에 따른 사용 가능 포인트 조회 로직 구현
        // 여기서는 단순화를 위해 예시값 반환
        return 100; // 예시: 사용자가 사용할 수 있는 포인트
    }

    // 획득 포인트 계산 예시 함수
    private int calculateEarnPoints(int totalPay) {
        // 구매 금액에 따른 포인트 적립 로직 구현
        // 여기서는 단순화를 위해 예시값 반환
        return totalPay / 10; // 예시: 결제 금액의 10%를 포인트로 적립
    }

//    private void decrementStockAndUpdateReport(List<CartItem> cartItems, List<CustomerOrderProduct> orderProducts) {
//        orderProducts.forEach(orderProduct -> {
//            CartItem matchingCartItem = cartItems.stream()
//                                                .filter(cartItem -> cartItem.getProductId().equals(orderProduct.getProductId()) && cartItem.getUserStoreId() != null)
//                                                .findFirst()
//                                                .orElse(null);
//
//            if (matchingCartItem != null) {
//                StoreStock currentStock = storeStockMapper.selectProductByUserStoreIdAndProductId(matchingCartItem.getUserStoreId(), orderProduct.getProductId());
//                if (currentStock != null && currentStock.getQty() >= orderProduct.getQty()) {
//                    // 재고 차감 (현재 재고 - 고객 주문 수량)
//                    int newQty = currentStock.getQty() - orderProduct.getQty();
//                    storeStockMapper.updateStockQty(currentStock.getUserStoreId(), orderProduct.getProductId(), newQty);
//
//                    // 재고 로그 기록
//                    StoreStockReport stockReport = new StoreStockReport();
//                    stockReport.setUserStoreId(matchingCartItem.getUserStoreId());
//                    stockReport.setProductId(orderProduct.getProductId());
//                    stockReport.setYmd(java.sql.Date.valueOf(LocalDate.now()));
//                    stockReport.setOutQty(orderProduct.getQty());
//                    storeStockMapper.insertStoreStockReport(stockReport);
//                } else {
//                    // 재고 부족 예외 처리
//                    throw new RuntimeException("재고가 부족합니다.");
//                }
//            }
//        });
//    }
}
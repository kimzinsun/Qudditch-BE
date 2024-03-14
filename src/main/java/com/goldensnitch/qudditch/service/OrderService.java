package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.*;
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
    private PaymentService paymentService;

    @Autowired
    private CartService cartService;

    @Autowired
    private StoreStockMapper storeStockMapper;

    @Transactional
    public String createOrder(Integer userCustomerId) {
        // 장바구니 아이템 조회
        List<CartItem> cartItems = cartService.getCartItem(userCustomerId);

        if (cartItems.isEmpty()) {
            return "장바구니가 비어 있습니다.";
        }

        // 총액 계산
        int totalAmount = calculateTotalAmount(cartItems);
        CustomerOrder customerOrder = setupCustomerOrder(userCustomerId, totalAmount);

        // 고객 주문 설정 (DB x)
        List<CustomerOrderProduct> orderProduct = setupCustomerOrderProduct(cartItems, customerOrder.getId());

        // 실제 결제에 필요한 정보 설정(결제 요청 -> PaymentService)
        PaymentRequest paymentRequest = createPaymentRequest(customerOrder, orderProduct);

        // 결제 처리 및 리다이렉트 URL 반환
        String paymentRedirectUrl = paymentService.initiatePayment(paymentRequest);

        // 결제 성공 품목을 DB에 저장(customer)
        customerOrderProductMapper.insertCustomerOrder(customerOrder);
        orderProduct.forEach(customerOrderProductMapper::insertCustomerOrderProduct);

        // 장바구니 비우기
        cartService.clearCart(userCustomerId);

        // 결제 성공 품목의 재고 차감
        decrementStockAndUpdateReport(cartItems, orderProduct);

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

    private CustomerOrder setupCustomerOrder(Integer userCustomerId, int totalAmount){
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setUserCustomerId(userCustomerId);
        customerOrder.setTotalAmount(totalAmount);
        customerOrder.setOrderedAt(LocalDate.now()); // LocalDate 사용

        customerOrderProductMapper.insertCustomerOrder(customerOrder);
        return customerOrder;
    }

    private List<CustomerOrderProduct> setupCustomerOrderProduct(List<CartItem> cartItems, Integer orderId){
        List<CustomerOrderProduct> customerOrderProduct = new ArrayList<>();
        cartItems.forEach(item -> {
            CustomerOrderProduct product = new CustomerOrderProduct();
            product.setCustomerOrderId(orderId);
            product.setProductId(item.getProductId());
            product.setQty(item.getQty());
            customerOrderProduct.add(product);
        });
        return customerOrderProduct;
    }

    private PaymentRequest createPaymentRequest(CustomerOrder customerOrder, List<CustomerOrderProduct> orderProduct) {
        PaymentRequest paymentRequest = new PaymentRequest();

        // 결제 요청에 필요한 정보 설정
        if (!orderProduct.isEmpty()) {
            // 상품명 설정
            Integer firstProductId = orderProduct.get(0).getProductId();
            Product product = productMapper.findByProductId(firstProductId);
            String itemName = product.getName();

            // 수량 설정
            int totalQuantity = orderProduct.stream().mapToInt(CustomerOrderProduct::getQty).sum();
            paymentRequest.setQuantity(totalQuantity);
        }

        // 총액, 포인트 등 설정
        paymentRequest.setTotal_amount(customerOrder.getTotalAmount());
        paymentRequest.setUsedPoint(customerOrder.getUsedPoint());
        paymentRequest.setTotalPay(customerOrder.getTotalPay());
        paymentRequest.setEarnPoint(customerOrder.getEarnPoint());

        // 비과세 null 체크: null값인 경우 0원으로 설정
        Integer taxFreeAmount = paymentRequest.getTax_free_amount();
        paymentRequest.setTax_free_amount(taxFreeAmount != null ? taxFreeAmount : 0); // 수정된 부분

        // CID, 주문 ID 등 설정
        paymentRequest.setCid("04CF21E2C19A63303B01"); // 카카오페이로부터 받은 가맹점 코드 설정
        paymentRequest.setPartner_order_id(customerOrder.getId().toString());
        paymentRequest.setPartner_user_id(customerOrder.getUserCustomerId().toString());

        // Redirect URL 설정 (실제 운영 환경에 맞게 수정)
        paymentRequest.setApproval_url("http://localhost:8080/approval");
        paymentRequest.setCancel_url("http://localhost:8080/cancel");
        paymentRequest.setFail_url("http://localhost:8080/fail");

        return paymentRequest;
    }

    private void decrementStockAndUpdateReport(List<CartItem> cartItems, List<CustomerOrderProduct> orderProducts) {
        orderProducts.forEach(orderProduct -> {
            CartItem matchingCartItem = cartItems.stream()
                                                .filter(cartItem -> cartItem.getProductId().equals(orderProduct.getProductId()) && cartItem.getUserStoreId() != null)
                                                .findFirst()
                                                .orElse(null);

            if (matchingCartItem != null) {
                StoreStock currentStock = storeStockMapper.selectProductByUserStoreIdAndProductId(matchingCartItem.getUserStoreId(), orderProduct.getProductId());
                if (currentStock != null && currentStock.getQty() >= orderProduct.getQty()) {
                    // 재고 차감 (현재 재고 - 고객 주문 수량)
                    int newQty = currentStock.getQty() - orderProduct.getQty();
                    storeStockMapper.updateStockQty(currentStock.getUserStoreId(), orderProduct.getProductId(), newQty);

                    // 재고 로그 기록
                    StoreStockReport stockReport = new StoreStockReport();
                    stockReport.setUserStoreId(matchingCartItem.getUserStoreId());
                    stockReport.setProductId(orderProduct.getProductId());
                    stockReport.setYmd(java.sql.Date.valueOf(LocalDate.now()));
                    stockReport.setOutQty(orderProduct.getQty());
                    storeStockMapper.insertStoreStockReport(stockReport);
                } else {
                    // 재고 부족 예외 처리
                    throw new RuntimeException("재고가 부족합니다.");
                }
            }
        });
    }
}
package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CartItem;
import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.dto.PaymentRequest;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    // DB에 주문 정보와 주문 상품 정보를 저장하기 위한 매퍼
    @Autowired
    private CustomerOrderProductMapper customerOrderProductMapper;

    // 상품 정보 조회를 위한 매퍼
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartService cartService;

    public String createOrder(Integer userCustomerId) {
        List<CartItem> cartItems = cartService.getCartItem(userCustomerId);

        if (cartItems.isEmpty()) {
            return "장바구니가 비어 있습니다.";
        }

        CustomerOrder customerOrder = new CustomerOrder();

        customerOrderProductMapper.insertCustomerOrder(customerOrder);
        Integer orderId = customerOrder.getId();

        List<CustomerOrderProduct> orderProduct = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            CustomerOrderProduct customerOrderProduct = new CustomerOrderProduct();
            customerOrderProduct.setCustomerOrderId(orderId);
            customerOrderProduct.setProductId(cartItem.getProductId());
            customerOrderProduct.setQty(cartItem.getQty());
            // Set any other necessary fields for CustomerOrderProduct

            customerOrderProductMapper.insertCustomerOrderProduct(customerOrderProduct);
            orderProduct.add(customerOrderProduct);
        }

        // PaymentRequest 생성 로직
        PaymentRequest paymentRequest = createPaymentRequest(customerOrder, orderProduct);

        // PaymentService를 사용하여 결제 처리
        String paymentRedirectUrl = paymentService.initiatePayment(paymentRequest);

        cartService.removeAllItemFromCart(userCustomerId);

        return paymentRedirectUrl;
    }
        /*
        // 주문 정보 생성 및 저장
        CustomerOrder customerOrder = orderRequest.getCustomerOrder();
        customerOrderProductMapper.insertCustomerOrder(customerOrder);
        Integer orderId = customerOrder.getId(); // DB 저장 후 생성된 주문

        // 주문 상품 정보 생성 및 저장
        for (CustomerOrderProduct product : orderRequest.getCustomerOrderProducts()) {
            product.setCustomerOrderId(orderId);
            customerOrderProductMapper.insertCustomerOrderProduct(product);
        }

        // PaymentRequest 생성 로직 (여기서 필요한 정보를 설정합니다.)
        PaymentRequest paymentRequest = createPaymentRequest(customerOrder, orderRequest.getCustomerOrderProducts());

        // PaymentService를 사용하여 결제 처리
        String paymentRedirectUrl = paymentService.initiatePayment(paymentRequest);

        return paymentRedirectUrl;
        */

    private PaymentRequest createPaymentRequest(CustomerOrder customerOrder, List<CustomerOrderProduct> orderProduct) {
        PaymentRequest paymentRequest = new PaymentRequest();

        return paymentRequest;

        /*
        // 상품 이름과 수량 설정 (여기서는 단순화를 위해 첫 번째 상품의 이름과 전체 수량만을 사용)
        if (!products.isEmpty()) {
            CustomerOrderProduct firstProduct = products.get(0);
            String itemName = productMapper.findByProductId(firstProduct.getProductId()).getName();
            Integer quantity = products.stream().mapToInt(CustomerOrderProduct::getQty).sum();

            paymentRequest.setItem_name(itemName);
            paymentRequest.setQuantity(quantity);
        }

        // 총액, 고유 번호 등 설정
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
    */
    }
}

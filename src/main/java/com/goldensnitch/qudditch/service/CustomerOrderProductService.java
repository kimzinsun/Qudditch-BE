package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.dto.payment.OrderResponse;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerOrderProductService { // 영수증 정보 생성, 월별 주문내역 확인

    private final CustomerOrderProductMapper customerOrderProductMapper;

    @Autowired
    public CustomerOrderProductService(CustomerOrderProductMapper customerOrderProductMapper) {
        this.customerOrderProductMapper = customerOrderProductMapper;
    }

//    public OrderResponse generateReceipt(Integer orderId) {
//        // 주문 정보 조회
//        CustomerOrder customerOrder = customerOrderProductMapper.findById(orderId);
//        if (customerOrder == null) {
//            throw new RuntimeException("Order not found with ID: " + orderId);
//        }
//        // 해당 주문의 상품 정보 조회
//        List<CustomerOrderProduct> customerOrderProducts = customerOrderProductMapper.findOrderProductsByOrderId(orderId);
//
//        OrderResponse receipt = new OrderResponse();
//        receipt.setCustomerOrder(customerOrder);
//        receipt.setCustomerOrderProducts(customerOrderProducts);
//
//        return receipt;
//    }

    public OrderResponse generateReceiptByPartnerOrderId(String partnerOrderId) {
        // 주문 정보 조회
        CustomerOrder customerOrder = customerOrderProductMapper.findByPartnerOrderId(partnerOrderId);
        if (customerOrder == null) {
            throw new RuntimeException("Order not found with partner_order_id: " + partnerOrderId);
        }
        // 해당 주문의 상품 정보 조회
        List<CustomerOrderProduct> customerOrderProducts = customerOrderProductMapper.findOrderProductsByPartnerOrderId(partnerOrderId);

        OrderResponse receipt = new OrderResponse();
        receipt.setCustomerOrder(customerOrder);
        receipt.setCustomerOrderProducts(customerOrderProducts);

        return receipt;
    }

    public List<OrderResponse> getMonthlyOrderHistory(String monthYear) {
        // 주문 내역 조회
        List<CustomerOrder> customerOrders = customerOrderProductMapper.findByMonthYear(monthYear);
        List<OrderResponse> monthlyOrderHistory = customerOrders.stream().map(order -> {
            List<CustomerOrderProduct> orderProducts = customerOrderProductMapper.findOrderProductsByOrderId(order.getId());
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setCustomerOrder(order);
            orderResponse.setCustomerOrderProducts(orderProducts);
            return orderResponse;
        }).collect(Collectors.toList());

        return monthlyOrderHistory;
    }

    public List<CustomerOrder> getPointHistoryByCustomerId(Integer userCustomerId) {
        return customerOrderProductMapper.findPointHistoryByCustomerId(userCustomerId);
    }
}

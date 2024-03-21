package com.goldensnitch.qudditch.service;

import org.springframework.stereotype.Service;

@Service
public class CustomerOrderProductService { // 영수증 정보 생성, 월별 주문내역 확인

//    @Autowired
//    private CustomerOrderProductMapper customerOrderProductMapper;

//    public OrderResponse generateReceipt(Integer orderId) {
//        // 주문 정보 조회
//        CustomerOrder customerOrder = customerOrderProductMapper.findById(orderId);
//        // 해당 주문의 상품 정보 조회
//        List<CustomerOrderProduct> products = customerOrderProductMapper.findByOrderId(orderId);
//
//        OrderResponse receipt = new OrderResponse();
//        receipt.setCustomerOrder(customerOrder);
//        receipt.setCustomerOrderProducts(products);
//
//        return receipt;
//    }
//
//    public List<OrderResponse> getMonthlyOrderHistory(Integer userCustomerId, String monthYear){
//        // 주문 내역 조회
//        List<CustomerOrder> customerOrders = customerOrderProductMapper.findByUserCustomerId(userCustomerId, monthYear);
//        return customerOrders.stream().map(order -> {
//            List<CustomerOrderProduct> products = customerOrderProductMapper.findByOrderId(order.getId());
//            OrderResponse orderResponse = new OrderResponse();
//            orderResponse.setCustomerOrder(order);
//            orderResponse.setCustomerOrderProducts(products);
//            return orderResponse;
//        }).collect(Collectors.toList());
//    }
}

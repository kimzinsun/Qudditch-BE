package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.dto.OrderRequest;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerOrderProductService { // 영수증 정보 생성, 월별 주문내역 확인

    @Autowired
    private CustomerOrderProductMapper customerOrderProductMapper;

    public OrderRequest generateReceipt(Integer orderId) {
        // 주문 정보 조회
        CustomerOrder customerOrder = customerOrderProductMapper.findById(orderId);
        // 해당 주문의 상품 정보 조회
        List<CustomerOrderProduct> products = customerOrderProductMapper.findByOrderId(orderId);

        // OrderRequest 객체로 묶기
        OrderRequest receipt = new OrderRequest();
        receipt.setCustomerOrder(customerOrder);
        receipt.setCustomerOrderProducts(products);

        return receipt;
    }

    public List<OrderRequest> getMonthlyOrderHistory(Integer userCustomerId, String monthYear){
        // 주문 내역 조회
        List<CustomerOrder> customerOrders = customerOrderProductMapper.findByUserCustomerId(userCustomerId, monthYear);

        // OrderRequest 객체 생성
        List<OrderRequest> monthlyHistory = customerOrders.stream()
                .map(order -> {
                    List<CustomerOrderProduct> customerOrderProducts = customerOrderProductMapper.findByOrderId(order.getId());
                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.setCustomerOrder(order);
                    orderRequest.setCustomerOrderProducts(customerOrderProducts);
                    return orderRequest;
                }).collect(Collectors.toList());

        return monthlyHistory;
    }
}

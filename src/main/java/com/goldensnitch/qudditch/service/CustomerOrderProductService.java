package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import com.goldensnitch.qudditch.mapper.CustomerOrderProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerOrderProductService {

    // private final CustomerOrderMapper customerOrderMapper;
    private final CustomerOrderProductMapper customerOrderProductMapper;

    @Autowired
    public CustomerOrderProductService(CustomerOrderProductMapper customerOrderProductMapper) {
        this.customerOrderProductMapper = customerOrderProductMapper;
    }

    @Transactional
    public void createCustomerOrder(CustomerOrder customerOrder, List<CustomerOrderProduct> customerOrderProducts) {
        // 주문 정보 저장
        customerOrderProductMapper.insertCustomerOrder(customerOrder);
        // 생성된 주문ID 가져오기
        Integer orderId = customerOrder.getId();
        
        // 주문 상품 정보 저장
        for (CustomerOrderProduct product : customerOrderProducts) {
            // 주문 상품에 주문 ID 설정
            product.setCustomerOrderId(orderId);
            customerOrderProductMapper.insertCustomerOrderProduct(product);
        }
    }

}
    /*
    private final Map<Integer, Map<Integer, Integer>> customerOrderDetails = new ConcurrentHashMap<>();

    public void addItemToCustomerOrder(Integer customerOrderId, Integer productId, Integer qty) {
        customerOrderDetails.computeIfAbsent(customerOrderId, k -> new ConcurrentHashMap<>()).merge(productId, qty, Integer::sum);
    }

    public Map<Integer, Map<Integer, Integer>> getCustomerOrderDetails(Integer customerOrderId) {
        return customerOrderDetails;
    }
    */


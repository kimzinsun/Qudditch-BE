package com.goldensnitch.qudditch.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerOrderProductService {
    private final Map<Integer, Map<Integer, Integer>> customerOrderDetails = new ConcurrentHashMap<>();

    public void addItemToCustomerOrder(Integer customerOrderId, Integer productId, Integer qty) {
        customerOrderDetails.computeIfAbsent(customerOrderId, k -> new ConcurrentHashMap<>()).merge(productId, qty, Integer::sum);
    }

    public Map<Integer, Map<Integer, Integer>> getCustomerOrderDetails(Integer customerOrderId) {
        return customerOrderDetails;
    }
}

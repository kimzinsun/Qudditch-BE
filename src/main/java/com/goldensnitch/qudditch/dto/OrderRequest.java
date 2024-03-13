package com.goldensnitch.qudditch.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private CustomerOrder customerOrder;
    private List<CustomerOrderProduct> customerOrderProducts;
}



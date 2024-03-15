package com.goldensnitch.qudditch.dto.payment;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.dto.CustomerOrderProduct;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private CustomerOrder customerOrder;
    private List<CustomerOrderProduct> customerOrderProducts;
}



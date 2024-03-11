package com.goldensnitch.qudditch.dto.StoreOder;

import com.goldensnitch.qudditch.dto.StoreOrder;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailWithProducts {
    StoreOrder storeOrder;
    List<ProductWithQty> products;


    public OrderDetailWithProducts(StoreOrder storeOrder, ProductWithQty productWithQty) {
        this.storeOrder = storeOrder;
        this.products = List.of(productWithQty);
    }
}

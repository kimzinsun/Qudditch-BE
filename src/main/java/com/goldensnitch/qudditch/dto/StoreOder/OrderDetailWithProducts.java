package com.goldensnitch.qudditch.dto.StoreOder;

import com.goldensnitch.qudditch.dto.StoreOrder;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailWithProducts {
    StoreOrder storeOrder;
    List<ProductWithDetailQty> products;


    public OrderDetailWithProducts(StoreOrder storeOrder, ProductWithDetailQty productWithQty) {
        this.storeOrder = storeOrder;
        this.products = List.of(productWithQty);
    }
}

package com.goldensnitch.qudditch.dto.StoreOder;

import com.goldensnitch.qudditch.dto.Product;
import lombok.Data;

@Data
public class ProductWithDetailQty extends Product {
    private Integer qty;
}

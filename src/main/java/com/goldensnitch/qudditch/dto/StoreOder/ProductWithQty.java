package com.goldensnitch.qudditch.dto.StoreOder;

import com.goldensnitch.qudditch.dto.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductWithQty extends Product {
    private Integer qty;
}

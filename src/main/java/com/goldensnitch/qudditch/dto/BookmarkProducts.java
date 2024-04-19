package com.goldensnitch.qudditch.dto;

import com.goldensnitch.qudditch.dto.CustomerBookmarkProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookmarkProducts extends CustomerBookmarkProduct {
//    private Integer id;
//    private Integer userCustomerId;
//    private Integer productId;
    private String productName;
    private String productBrand;
    private String productImage;
    private Integer productPrice;
}

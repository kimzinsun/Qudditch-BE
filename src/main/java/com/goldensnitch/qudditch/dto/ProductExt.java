package com.goldensnitch.qudditch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductExt extends Product {
    private boolean isBookmark;
}

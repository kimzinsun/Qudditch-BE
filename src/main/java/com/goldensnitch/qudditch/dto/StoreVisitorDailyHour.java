package com.goldensnitch.qudditch.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoreVisitorDailyHour extends StoreSalesDaily {
    private Integer hour;
}

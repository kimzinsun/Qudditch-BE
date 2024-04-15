package com.goldensnitch.qudditch.dto;
import lombok.Data;

import java.util.Date;

@Data
public class point {
    private String name;
    private Integer earnPoint;
    private Integer usedPoint;
    private Date orderedAt;
}

package com.goldensnitch.qudditch.dto;

import lombok.Data;

@Data
public class ChatbotDto {

    private String msg;
    private double currentWgs84X;
    private double currentWgs84Y;
}

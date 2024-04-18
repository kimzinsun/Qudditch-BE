package com.goldensnitch.qudditch.controller;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Test implements Principal {
    private String id;
    private String name;
    private String email;

    @Override
    public String getName() {
        return name;
    }
}
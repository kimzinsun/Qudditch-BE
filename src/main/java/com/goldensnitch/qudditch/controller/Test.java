package com.goldensnitch.qudditch.controller;

import java.security.Principal;

public class Test implements Principal {
    private String userName;
    private String email;

    public Test(String username, String email) {
        this.userName = username;
        this.email = email;
    }

    @Override
    public String getName() {
        return userName;
    }

   public String getEmail(){
    return email;
   }
}
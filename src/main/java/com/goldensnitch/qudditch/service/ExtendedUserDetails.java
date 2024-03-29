package com.goldensnitch.qudditch.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtendedUserDetails extends User {
    private Integer id;
    private String name;
    private String email;

    // 추가 필드가 있다면 여기에 선언하세요.

    public ExtendedUserDetails(String username, String password,
                            Collection<? extends GrantedAuthority> authorities,
                            Integer id, String name, String email) {
        super(username, password, authorities);
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    // 필요한 메소드들을 여기에 더 작성하세요.
}

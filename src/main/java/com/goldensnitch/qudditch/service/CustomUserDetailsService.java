package com.goldensnitch.qudditch.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.goldensnitch.qudditch.model.UserCustomer;
import com.goldensnitch.qudditch.repository.UserCustomerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    
    @Autowired
    private UserCustomerRepository userCustomerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCustomer userCustomer = userCustomerRepository.selectUserByEmail(email);
        if (userCustomer == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(userCustomer.getEmail(),
                userCustomer.getPassword(), new ArrayList<>());
    }

}
package com.goldensnitch.qudditch.controller;


import com.goldensnitch.qudditch.dto.UserStore;
import com.goldensnitch.qudditch.service.BestProductService;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class BestProductController {

    private final BestProductService bestProductService;

    @Autowired
    public BestProductController(BestProductService bestProductService) {

        this.bestProductService = bestProductService;
    }

    @GetMapping("/BestProduct")
    public Map<String,Object> BestProduct(@AuthenticationPrincipal ExtendedUserDetails userDetails) {

        int Id = userDetails.getId();
        List<UserStore> list = bestProductService.BestProduct(Id);

        return Map.of(
                "bestProducts", list
        );
    }
}

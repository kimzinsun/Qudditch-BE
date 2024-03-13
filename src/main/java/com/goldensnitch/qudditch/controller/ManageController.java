package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.manage.OrderDetailRes;
import com.goldensnitch.qudditch.dto.manage.OrderRes;
import com.goldensnitch.qudditch.service.ManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/manage")
public class ManageController {
    private final ManageService manageService;

    public ManageController(ManageService manageService) {
        this.manageService = manageService;
    }

    @GetMapping("/order")
    public List<OrderRes> getOrderList() {
        return manageService.getOrderList();
    }

    @GetMapping("/order/detail/{orderStoreId}")
    public List<OrderDetailRes> getOrderDetail(@PathVariable int orderStoreId) {
        return manageService.getOrderDetail(orderStoreId);
    }
}

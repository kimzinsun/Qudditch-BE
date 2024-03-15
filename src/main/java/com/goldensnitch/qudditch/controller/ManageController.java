package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Pagination;
import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.manage.InputReq;
import com.goldensnitch.qudditch.dto.manage.OrderDetailRes;
import com.goldensnitch.qudditch.dto.manage.OrderRes;
import com.goldensnitch.qudditch.service.ManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/manage")
public class ManageController {
    private final ManageService manageService;

    public ManageController(ManageService manageService) {
        this.manageService = manageService;
    }

    @GetMapping("/order")
    public Map<String, Object> getOrderList(PaginationParam paginationParam) {
        int count = manageService.getOrderCount();
        List<OrderRes> list = manageService.getOrderList(paginationParam);
        Pagination pagination = new Pagination(count, paginationParam);

        return Map.of("orderList", list, "pagination", pagination);

    }

    @GetMapping("/order/detail/{orderStoreId}")
    public List<OrderDetailRes> getOrderDetail(@PathVariable int orderStoreId) {
        return manageService.getOrderDetail(orderStoreId);
    }

    @PostMapping("/order/detail/{orderStoreId}")
    public void confirmOrder(@PathVariable int orderStoreId, @RequestBody List<InputReq> list) {
        manageService.confirmOrder(orderStoreId, list);
    }

}

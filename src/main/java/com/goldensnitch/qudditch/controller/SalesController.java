package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerOrder;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.SalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    @Autowired
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/DailySales")
    public List<CustomerOrder> DailySales(@RequestParam(value = "orderedAt") String orderedAt,
                                          @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return salesService.DailySales(Date.valueOf(orderedAt), null);
        }
        int userStoreId = userDetails.getId();

        // String으로 받은 날짜데이터를 date타입으로 변환.
        Date date = Date.valueOf(orderedAt);

        List<CustomerOrder> list = salesService.DailySales(date, userStoreId);

        log.info("list: {}", list);

        return list;
    }


    @GetMapping("/MonthlySales")
    public List<CustomerOrder> MonthlySales(@RequestParam(value = "yearMonth") String yearMonth,
                                            @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return salesService.MonthlySales(yearMonth, null);
        }

        int userStoreId = userDetails.getId();

        List<CustomerOrder> list = salesService.MonthlySales(yearMonth, userStoreId);

        return list;
    }
}

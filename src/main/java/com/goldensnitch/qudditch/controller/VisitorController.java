package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/visitor")
public class VisitorController {
    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    // TODO : visitor 관련 기능 구현
    @GetMapping("/daily")
    public Integer getDailyVisitor(@RequestParam String date) {
        int storeId = 2;
        // int storeId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return visitorService.getDailyVisitor(storeId, date);
    }

}

package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Integer getDailyVisitor(@AuthenticationPrincipal ExtendedUserDetails userDetails, @RequestParam String date) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return visitorService.getDailyVisitor(null, date);
        }
        int userStoreId = userDetails.getId();

        return visitorService.getDailyVisitor(userStoreId, date);
    }

    @GetMapping("/month")
    public Integer getMonthVisitor(@AuthenticationPrincipal ExtendedUserDetails userDetails, @RequestParam String yearMonth) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return visitorService.getMonthVisitor(null, yearMonth);
        }
        int userStoreId = userDetails.getId();

        return visitorService.getMonthVisitor(userStoreId, yearMonth);
    }

}

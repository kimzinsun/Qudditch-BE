package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.graph.CategoryGraphDto;
import com.goldensnitch.qudditch.dto.graph.SalesGraphDto;
import com.goldensnitch.qudditch.dto.graph.VisitorGraphDto;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService service;

    @Autowired
    public GraphController(GraphService service) {
        this.service = service;
    }

    @GetMapping("/sales")
    // @RequestParam
    public SalesGraphDto getSalesGraph(@AuthenticationPrincipal ExtendedUserDetails userDetails, String yearMonth){
        int userStoreId = userDetails.getId();

        return service.getSalesGraph(userStoreId, yearMonth);
    }

    @GetMapping("/category")
    public CategoryGraphDto getCategoryGraph(@AuthenticationPrincipal ExtendedUserDetails userDetails, String yearMonth){
        int userStoreId = userDetails.getId();

        return service.getCategoryGraph(userStoreId, yearMonth);
    }

    @GetMapping("/visitor")
    public VisitorGraphDto getVisitorGraph(@AuthenticationPrincipal ExtendedUserDetails userDetails, String yearMonth){
        int userStoreId = userDetails.getId();

        return service.getVisitorGraph(userStoreId, yearMonth);
    }

}

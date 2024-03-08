package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.graph.SalesGraphDto;
import com.goldensnitch.qudditch.service.GraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService service;

    @Autowired
    public GraphController(GraphService service) {
        this.service = service;
    }

    @GetMapping("/dest")
    public String test(){
        System.out.println("test");
        return "test ok" + service.test();
    }

    @GetMapping("/sales/{userStoreId}")
    public SalesGraphDto getSalesGraph(@PathVariable Integer userStoreId){
        log.info("GraphController.getSalesGraph: {}", userStoreId);

        return service.getSalesGraph(userStoreId);
    }
}

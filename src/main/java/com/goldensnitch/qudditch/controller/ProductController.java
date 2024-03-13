package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.CrawlingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/crawl")
public class ProductController {

    private final CrawlingService crawlingService;

    public ProductController(CrawlingService crawlingService) {
        this.crawlingService = crawlingService;
    }

//    실행시키지 마세용 ~
//    @GetMapping("/crawl")
//    public void crawl() {
//        String url = "https://emart.ssg.com/disp/theme/category.ssg?dispCtgId=6000223611&page=3";
//        crawlingService.Crawling(url);
//
//    }
}

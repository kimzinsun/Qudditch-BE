package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.service.CrawlingService;
import com.goldensnitch.qudditch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;



    private final CrawlingService crawlingService;

    public ProductController(ProductService productService, CrawlingService crawlingService) {
        this.productService = productService;
        this.crawlingService = crawlingService;
    }


    @GetMapping("/detail/{productId}")
    public Product selectProductById(@PathVariable Integer productId) {
        return productService.selectProductById(productId);
    }

//    실행시키지 마세용 ~
//    @GetMapping("/crawl")
//    public void crawl() {
//        String url = "https://emart.ssg.com/disp/theme/category.ssg?dispCtgId=6000223611&page=3";
//        crawlingService.Crawling(url);
//
//    }
}

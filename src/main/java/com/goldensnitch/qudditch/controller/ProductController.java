package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.service.CrawlingService;
import com.goldensnitch.qudditch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/crawl")
public class ProductController {

    private final CrawlingService crawlingService;

    // 규원
    private final ProductService productService;

    public ProductController(CrawlingService crawlingService, ProductService productService) {
        this.crawlingService = crawlingService;
        // 규원
        this.productService = productService;
    }

//    실행시키지 마세용 ~
//    @GetMapping("/crawl")
//    public void crawl() {
//        String url = "https://emart.ssg.com/disp/theme/category.ssg?dispCtgId=6000223611&page=3";
//        crawlingService.Crawling(url);
//
//    }

    // 규원
    @GetMapping("/product/details/{productId}")
    // 포스트맨 주소연결: {{API_URL}}/api/crawl/product/details/343
    public ResponseEntity<?> getProductDetailsByProductId(@PathVariable Integer productId){
        try{
            Map<String, Object> productDetails = productService.getProductDetailsByProductId(productId);
            return ResponseEntity.ok(productDetails);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
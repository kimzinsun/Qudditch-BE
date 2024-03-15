package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.StoreStock;
import com.goldensnitch.qudditch.service.CrawlingService;
import com.goldensnitch.qudditch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;

    }


    @GetMapping("/detail/{productId}")
    public Product selectProductById(@PathVariable Integer productId) {
        return productService.selectProductById(productId);
    }

    @GetMapping("/find/{productName}")
    public List<Product> selectProductByName(@PathVariable String productName) {
        return productService.selectProductByName(productName);
    }

    @GetMapping("/store/{productId}")
    public List<StoreStock> selectStoreStockByProductId(@PathVariable Integer productId, @RequestParam double currentWgs84X, @RequestParam double currentWgs84Y) {
        return productService.selectStoreStockByProductId(productId, currentWgs84X, currentWgs84Y);
    }

}

package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.Pagination;
import com.goldensnitch.qudditch.dto.PaginationParam;
import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.StoreStock;
import com.goldensnitch.qudditch.service.CrawlingService;
import com.goldensnitch.qudditch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // selectProductByName로 먼저 이름을 검색해서 productId를 가져온 후에 selectStoreStockByProductId로 호출하는 방식으로 변경

    @GetMapping("/store/{productId}")
    public Map<String, Object> selectStoreStockByProductId(@PathVariable Integer productId, PaginationParam paginationParam){
//        double currentWgs84x = SecurityContextHolder.getContext().getAuthentication().getPrincipal().getCurrentX();
//        double currentWgs84y = SecurityContextHolder.getContext().getAuthentication().getPrincipal().getCurrentY();
        double currentWgs84X = 128.91231380719825;
        double currentWgs84Y = 37.79691574964818;

        int count = productService.cntStoreStockByProductId(productId);
        List<StoreStock> storeStockList = productService.selectStoreStockByProductId(productId, currentWgs84X, currentWgs84Y, paginationParam);
        Pagination pagination = new Pagination(count, paginationParam);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pagination", pagination);
        map.put("storeStockList", storeStockList);

        return map;
    }

}

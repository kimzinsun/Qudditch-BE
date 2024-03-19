package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.CustomerBookmarkProduct;
import com.goldensnitch.qudditch.service.BookmarkUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookmark/user")
public class BookmarkUserController {

    @Autowired
    BookmarkUserService service;

    @GetMapping("/products")
    public List<CustomerBookmarkProduct> getProduct(int userCustomerId){
        log.info("BookmarkUserController.getProduct: userCustomerId {}", userCustomerId);

        return service.getProducts(userCustomerId);
    }

    @PostMapping("/product")
    public String createProduct(@RequestBody CustomerBookmarkProduct dto) {
        log.info("BookmarkUserController.createProduct: CustomerBookmarkProduct {}", dto);

        return service.createProduct(dto) ? "SUCCESS" : "FAIL";
    }

    @DeleteMapping("/product")
    public String removeProduct(CustomerBookmarkProduct dto) {
        log.info("BookmarkUserController.removeProduct: CustomerBookmarkProduct {}", dto);

        return service.deleteProduct(dto) ? "SUCCESS" : "FAIL";
    }
}

package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import com.goldensnitch.qudditch.utils.Crawling;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlingService {
    ProductMapper productMapper;

    public CrawlingService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public void Crawling(String url) {
        List<Product> list = Crawling.Crawling(url);
        for (Product product : list) {
            System.out.println(product);
            productMapper.insertProduct(product.getCategoryId(),product.getBrand(), product.getName(), product.getDescription(), product.getImage(), product.getPrice(), product.getUnitPrice(), product.getExpirationDate());
        }


    }
}

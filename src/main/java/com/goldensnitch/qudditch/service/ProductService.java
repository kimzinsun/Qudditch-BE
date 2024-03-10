package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Product selectProductById(Integer id) {
        return productMapper.selectProductById(id);
    }
}

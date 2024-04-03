package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductExt selectProductById(Integer productId, Integer userId) {
        return productMapper.selectProductById(productId, userId);
    }

    public List<Product> selectProductByName(String productName) {
        return productMapper.selectProductByName(productName);
    }

    public List<StoreStock> selectStoreStockByProductId(Integer productId, double currentWgs84X, double currentWgs84Y, PaginationParam paginationParam) {
        return productMapper.selectStoreStockByProductId(productId, currentWgs84X, currentWgs84Y, paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public int cntStoreStockByProductId(Integer productId) {
        return productMapper.cntStoreStockByProductId(productId);
    }

    public List<ProductRank> getBestProductList() {
        return productMapper.getBestProductList();
    }

}

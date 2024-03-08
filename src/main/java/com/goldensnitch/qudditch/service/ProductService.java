package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.StoreOrderProduct;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import com.goldensnitch.qudditch.mapper.StoreOrderProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final StoreOrderProductMapper storeOrderProductMapper;

    @Autowired
    public ProductService(ProductMapper productMapper, StoreOrderProductMapper storeOrderProductMapper) {
        this.productMapper = productMapper;
        this.storeOrderProductMapper = storeOrderProductMapper;
    }

    /*
    public Product findByProductId(Integer productId) {
        return productMapper.findByProductId(productId);
    }
     */

    public Map<String, Object> getProductDetailsByProductId(Integer productId) throws IllegalAccessException {
        // 제품 정보 조회
        Product product = productMapper.findByProductId(productId);

        if(product == null){
            // 제품 정보가 없는 경우, 적절한 예외처리/오류 메세지 반환
            throw new IllegalAccessException("Product with ID " + productId + " not found.");
        }
        // 재고 정보 조회
        StoreOrderProduct stock = Optional.ofNullable( storeOrderProductMapper.findByStoreProductId(productId))
                .orElse(new StoreOrderProduct()); // 재고 정보가 없는 경우 빈 객체를 사용한다

        // 정보 조합
        Map<String, Object> detail = new HashMap<>();
        detail.put("id", product.getId());
        detail.put("categoryId", product.getCategoryId());
        detail.put("brand", product.getBrand());
        detail.put("name", product.getName());
        detail.put("price", product.getPrice());
        detail.put("unitPrice", product.getUnitPrice());
        detail.put("qty", stock.getQty()); // 재고 수량 추가

        return detail;
    }
}

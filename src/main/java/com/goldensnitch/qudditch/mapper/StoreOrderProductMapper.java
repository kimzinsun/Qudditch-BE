package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreOrderProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreOrderProductMapper {

    // void insertStoreOrderProduct(@Param("storeOrderProduct") StoreOrderProduct storeOrderProduct);

    /*
    // 조회
    // ProductId를 찾아서 StoreStock 정보를 관리하는 기능
    // QR code: 프론트에서 구현, productId를 인식한 결과를 백엔드로 전송
    StoreOrderProduct findByStoreProductId(@Param("ProductId") Integer productId);
     */
    StoreOrderProduct findByStoreProductId(Integer productId);
}
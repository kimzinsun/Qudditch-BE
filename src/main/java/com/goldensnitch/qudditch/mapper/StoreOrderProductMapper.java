package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.StoreOrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StoreOrderProductMapper {

/*
    // 조회
    // ProductId를 찾아서 StoreStock 정보를 관리하는 기능
    // QR code: 프론트에서 구현, productId를 인식한 결과를 백엔드로 전송
    StoreOrderProduct findByProductId(@Param("ProductId") Integer productId);

 */

    // 등록
    void insertStoreOrderProduct(@Param("storeOrderProduct") StoreOrderProduct storeOrderProduct);

    StoreOrderProduct findByProductId(Integer productId);
}

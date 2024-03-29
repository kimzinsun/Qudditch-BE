package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.CustomerBookmarkProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookmarkUserMapper {
    List<CustomerBookmarkProduct> selectProducts(int userCustomerId);
    int insertProduct(CustomerBookmarkProduct dto);
    int countProduct(CustomerBookmarkProduct dto);
    int deleteProduct(CustomerBookmarkProduct dto);
}

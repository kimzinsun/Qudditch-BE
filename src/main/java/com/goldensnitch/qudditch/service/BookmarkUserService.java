package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.CustomerBookmarkProduct;
import com.goldensnitch.qudditch.mapper.BookmarkUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkUserService {

    @Autowired
    BookmarkUserMapper mapper;

    public List<CustomerBookmarkProduct> getProducts(int userCustomerId){
        return mapper.selectProducts(userCustomerId);
    }

    public boolean createProduct(CustomerBookmarkProduct dto){
        return mapper.insertProduct(dto) > 0;
    }

    public  boolean deleteProduct(CustomerBookmarkProduct dto){
        return mapper.deleteProduct(dto) > 0;
    }

    public boolean isDuplicateProduct(CustomerBookmarkProduct dto){
        return mapper.countProduct(dto) > 0;
    }
}

package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.Product;
import com.goldensnitch.qudditch.dto.UserStoreExt;
import com.goldensnitch.qudditch.dto.manage.InputOrder;
import com.goldensnitch.qudditch.dto.manage.OrderDetailRes;
import com.goldensnitch.qudditch.dto.manage.OrderRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ManageMapper {
    List<OrderRes> getOrderList(int recordSize, int offset);

    List<OrderDetailRes> getOrderDetail(int orderStoreId);

    void updateOrderState(int orderStoreId);

    void insertStoreInput(Map<String, Object> map);

    void insertStoreInputProduct(InputOrder inputOrder);

    int getUserStoreIdByOrderId(int orderStoreId);

    int getOrderCount();


    int getStoreCount();

    List<UserStoreExt> getStoreList(Integer recordSize, Integer offset);

    int getProductCountByCategoryId(Integer categoryId);

    List<Product> getProductListByCategoryId(Integer recordSize, Integer offset, Integer categoryId);

    List<Product> getProductList(Integer recordSize, Integer offset);

    int getProductCount();

    void addProduct(Product product);

    void updateProduct(int productId, Product product);
}

package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.dto.storeInput.InputDetailRes;
import com.goldensnitch.qudditch.dto.storeInput.InputRepoReq;
import com.goldensnitch.qudditch.dto.storeInput.InputRes;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

@Mapper
public interface StoreStockMapper {
    int cntProductByUserStoreId(int userStoreId);
    List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId);
    StoreStock selectProductByUserStoreIdAndProductId(int userStoreId, int productId);

    void updateStock(StoreStock storeStock);

    List<StoreStockRes> selectProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId);

    List<StoreLocQty> selectStoreByProductId(Integer productId, double currentWgs84X, double currentWgs84Y);

    int cntProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId);

    void insertDisposeLog(int userStoreId, int productId, int qty);

    int getDisposeLogCount(int userStoreId);

    List<DisposeLog> getDisposeLog(int userStoreId);
    List<InputRes> getOrderListByUserStoreId(int userStoreId);

    List<InputDetailRes> getOrderDetailByStoreInputId(int storeInputId);


    void insertStoreStock(int userStoreId, int productId, int positionId, int qty, String expiredAt);
    void updateConfirmInput(int storeInputId, int productId);
    void insertInputLog(InputRepoReq inputRepoReq);
    Date getInputDate(int storeInputId);

    void insertStoreStockReport(int userStoreId, int productId, java.sql.Date ymd, int outQty);

    Integer selectStockQtyByProductIdAndUserStoreId(int productId, int userStoreId);

    void updateStockQtyByProductIdAndUserStoreId(Integer productId, Integer userStoreId, Integer newStoreStock);

    int updateStoreStockReportOutQty(Integer userStoreId, Integer productId, Date date, Integer outQty);
}

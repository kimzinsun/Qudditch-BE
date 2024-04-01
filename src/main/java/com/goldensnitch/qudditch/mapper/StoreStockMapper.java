package com.goldensnitch.qudditch.mapper;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.dto.storeInput.InputDetailRes;
import com.goldensnitch.qudditch.dto.storeInput.InputRepoReq;
import com.goldensnitch.qudditch.dto.storeInput.InputRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface StoreStockMapper {
    int cntProductByUserStoreId(int userStoreId, String keyword);
    List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId);
    StoreStock selectProductByUserStoreIdAndProductId(int userStoreId, int productId);

    void updateStock(StoreStock storeStock);

    List<StoreStockRes> selectProductByUserStoreIdAndCategoryId(int userStoreId, String keyword, Integer categoryId ,int recordSize, int offset);

    List<StoreLocQty> selectStoreByProductId(int productName, double currentWgs84X, double currentWgs84Y);

    int cntProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId, String keyword);

    void insertDisposeLog(int userStoreId, int productId, int qty);

    int getDisposeLogCount(int userStoreId);

    List<DisposeLog> getDisposeLog(int userStoreId, int recordSize, int offset);
    List<InputRes> getOrderListByUserStoreId(int userStoreId, int recordSize, int offset);
    int cntOrderListByUserStoreId(int userStoreId);

    List<InputDetailRes> getOrderDetailByStoreInputId(int storeInputId);


    void insertStoreStock(int userStoreId, int productId, int positionId, int qty, String expiredAt);
    void updateConfirmInput(int storeInputId, int productId, int positionId);
    void insertInputLog(InputRepoReq inputRepoReq);
    Date getInputDate(int storeInputId);

    List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId, String keyword, int recordSize, int offset);
    int cntState(int storeInputId);

    void updateState(int storeInputId);

    List<Integer> getTargetAlertUserByProductIdAndStoreId(int productId, int userStoreId);

    int getUserStoreIdByInputId(int storeInputId);

    void insertStoreStockReport(int userStoreId, int productId, java.sql.Date ymd, int outQty);

    Integer selectStockQtyByProductIdAndUserStoreId(int productId, int userStoreId);

    void updateStockQtyByProductIdAndUserStoreId(Integer productId, Integer userStoreId, Integer newStoreStock);

    int updateStoreStockReportOutQty(Integer userStoreId, Integer productId, Date date, Integer outQty);
}

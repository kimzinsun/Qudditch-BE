package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.dto.storeInput.InputDetailRes;
import com.goldensnitch.qudditch.dto.storeInput.InputRepoReq;
import com.goldensnitch.qudditch.dto.storeInput.InputRes;
import com.goldensnitch.qudditch.dto.storeInput.StockInputReq;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreStockService {
    final StoreStockMapper storeStockMapper;

    public StoreStockService(StoreStockMapper storeStockMapper) {
        this.storeStockMapper = storeStockMapper;
    }

    public List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId) {
        return storeStockMapper.selectAllProductByUserStoreId(userStoreId);
    }

    public void updateStock(StoreStock storeStock) {
        storeStockMapper.updateStock(storeStock);
    }

    public StoreStock selectProductByUserStoreIdAndProductId(int userStoreId, int productId) {
        return storeStockMapper.selectProductByUserStoreIdAndProductId(userStoreId, productId);
    }

    public List<StoreStockRes> selectProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId, PaginationParam paginationParam) {
        return storeStockMapper.selectProductByUserStoreIdAndCategoryId(userStoreId, categoryId, paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public List<StoreLocQty> getStoreByProductId(int productName, double currentWgs84X, double currentWgs84Y) {
        return storeStockMapper.selectStoreByProductId(productName, currentWgs84X, currentWgs84Y);
    }

    public int cntProductByUserStoreId(int userStoreId) {
        return storeStockMapper.cntProductByUserStoreId(userStoreId);
    }

    public int cntProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId) {
        return storeStockMapper.cntProductByUserStoreIdAndCategoryId(userStoreId, categoryId);

    }

    public void insertDisposeLog(int userStoreId, Integer productId, Integer qty) {
        storeStockMapper.insertDisposeLog(userStoreId, productId, qty);
    }

    public int getDisposeLogCount(int userStoreId) {
        return storeStockMapper.getDisposeLogCount(userStoreId);
    }

    public List<DisposeLog> getDisposeLog(int userStoreId, PaginationParam paginationParam) {
        return storeStockMapper.getDisposeLog(userStoreId, paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public List<InputRes> getOrderListByUserStoreId(int userStoreId, PaginationParam paginationParam) {
        return storeStockMapper.getOrderListByUserStoreId(userStoreId, paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public int cntOrderListByUserStoreId(int userStoreId) {
        return storeStockMapper.cntOrderListByUserStoreId(userStoreId);
    }

    public List<InputDetailRes> getOrderDetailByStoreInputId(int storeInputId) {
        return storeStockMapper.getOrderDetailByStoreInputId(storeInputId);
    }

    public void insertStoreStock(int userStoreId, StockInputReq req, int storeInputId) {
        InputRepoReq inputRepoReq = new InputRepoReq();
        inputRepoReq.setUserStoreId(userStoreId);
        inputRepoReq.setProductId(req.getProductId());
        inputRepoReq.setYmd(storeStockMapper.getInputDate(storeInputId));
        inputRepoReq.setInQty(req.getQty());
        storeStockMapper.insertInputLog(inputRepoReq);

        storeStockMapper.updateConfirmInput(storeInputId, req.getProductId());
        storeStockMapper.insertStoreStock(userStoreId, req.getProductId(), req.getPositionId(), req.getQty(), String.valueOf(req.getExpiredAt()));

        if(storeStockMapper.cntState(storeInputId) == 0) {
            storeStockMapper.updateState(storeInputId);
        }
    }

    public List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId, PaginationParam paginationParam) {
        return storeStockMapper.selectAllProductByUserStoreId(userStoreId, paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    
}

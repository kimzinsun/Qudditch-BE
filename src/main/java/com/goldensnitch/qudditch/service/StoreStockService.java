package com.goldensnitch.qudditch.service;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.dto.fcm.FCMNotificationRequestDto;
import com.goldensnitch.qudditch.dto.storeInput.InputDetailRes;
import com.goldensnitch.qudditch.dto.storeInput.InputRepoReq;
import com.goldensnitch.qudditch.dto.storeInput.InputRes;
import com.goldensnitch.qudditch.dto.storeInput.StockInputReq;
import com.goldensnitch.qudditch.mapper.ProductMapper;
import com.goldensnitch.qudditch.mapper.StoreStockMapper;
import com.goldensnitch.qudditch.utils.ExcelCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class StoreStockService {
    final StoreStockMapper storeStockMapper;
    private final FCMNotificationService fcmNotificationService;
    private final ProductMapper productMapper;
    @Autowired
    ExcelCreator excelCreator;


    public StoreStockService(StoreStockMapper storeStockMapper, FCMNotificationService fcmNotificationService, ProductMapper productMapper) {
        this.storeStockMapper = storeStockMapper;
        this.fcmNotificationService = fcmNotificationService;
        this.productMapper = productMapper;
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
        return storeStockMapper.selectProductByUserStoreIdAndCategoryId(userStoreId,paginationParam.getKeyword(), categoryId,  paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public List<DisposalItem> getDisposeItemList(int userStoreId, PaginationParam paginationParam) {
        return storeStockMapper.getDisposeItemList(userStoreId, paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public int cntDisposeItem(int userStoreId) {
        return storeStockMapper.cntDisposeItem(userStoreId);
    }


    public int cntProductByUserStoreId(int userStoreId, String keyword) {
        return storeStockMapper.cntProductByUserStoreId(userStoreId, keyword);
    }

    public int cntProductByUserStoreIdAndCategoryId(int userStoreId, Integer categoryId, String keyword) {
        return storeStockMapper.cntProductByUserStoreIdAndCategoryId(userStoreId, categoryId, keyword);

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
        storeStockMapper.updateConfirmInput(storeInputId, req.getProductId(), req.getPositionId());
        storeStockMapper.insertStoreStock(userStoreId, req.getProductId(), req.getPositionId(), req.getQty(), String.valueOf(req.getExpiredAt()));
        if (storeStockMapper.cntState(storeInputId) == 0) {
            storeStockMapper.updateState(storeInputId);
        }

        List<Integer> userId = storeStockMapper.getTargetAlertUserByProductIdAndStoreId(req.getProductId(), userStoreId);

        for (Integer id : userId) {
            String productName = productMapper.selectProductNameByID(id);
            FCMNotificationRequestDto fcmNotificationRequestDto = new FCMNotificationRequestDto();
            fcmNotificationRequestDto.setTargetUserId(id);
            fcmNotificationRequestDto.setTitle("입고 알림");
            fcmNotificationRequestDto.setBody(productName+"이 입고되었습니다!");

        }
        InputRepoReq inputRepoReq = new InputRepoReq();
        inputRepoReq.setUserStoreId(userStoreId);
        inputRepoReq.setProductId(req.getProductId());
        inputRepoReq.setYmd(storeStockMapper.getInputDate(storeInputId));
        inputRepoReq.setInQty(req.getQty());
        storeStockMapper.insertInputLog(inputRepoReq);


    }

    public List<StoreStockRes> selectAllProductByUserStoreId(int userStoreId, PaginationParam paginationParam) {
        return storeStockMapper.selectAllProductByUserStoreId(userStoreId,paginationParam.getKeyword(), paginationParam.getRecordSize(), paginationParam.getOffset());
    }

    public int getUserStoreIdByInputId(int storeInputId) {
        return storeStockMapper.getUserStoreIdByInputId(storeInputId);
    }




    public ResponseEntity<ByteArrayResource> downloadInputList(Integer inputId) throws IOException {
        String fileName = "input";
        List<String> headers = new ArrayList<>();
        String[] headerArray = {"상품id", "브랜드", "상품명", "가격", "수량", "검수", "유통기한"};
        Collections.addAll(headers, headerArray);

        List<InputDetailRes> list = storeStockMapper.getOrderDetailByStoreInputId(inputId);
        List<List<String>> request = new ArrayList<>();
        for (InputDetailRes inputDetailRes : list) {
            List<String> innerList = new ArrayList<>();
            innerList.add(String.valueOf(inputDetailRes.getProductId()));
            innerList.add(inputDetailRes.getBrand());
            innerList.add(inputDetailRes.getName());
            innerList.add(String.valueOf(inputDetailRes.getPrice()));
            innerList.add(String.valueOf(inputDetailRes.getQty()));
            innerList.add(inputDetailRes.getState());
            innerList.add(inputDetailRes.getExpDate());
            request.add(innerList);
        }

         return excelCreator.downloadOrderDataAsExcel(fileName, headers, request);
    }

    public DisposalItem getDisposeItemByStoreStockId(Integer productId, Integer userStoreId) {
        return storeStockMapper.getDisposeItemByStoreStockId(productId, userStoreId);
    }

    public void updateDispose(Integer storeStockId, Integer userStoreId) {
        storeStockMapper.updateDispose(storeStockId, userStoreId);
    }
}

package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.dto.storeInput.InputDetailRes;
import com.goldensnitch.qudditch.dto.storeInput.InputRes;
import com.goldensnitch.qudditch.dto.storeInput.StockInputReq;
import com.goldensnitch.qudditch.service.StoreStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/store")
public class StoreStockController {
    private final StoreStockService storeStockService;

    public StoreStockController(StoreStockService storeStockService) {
        this.storeStockService = storeStockService;
    }
    // TODO : store 관련 기능 구현

    @GetMapping("/stock")
    public Map<String, Object> getStockList(@RequestParam @Nullable Integer categoryId, PaginationParam paginationParam) {
//        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userStoreId = 2;

        int count = categoryId == null ? storeStockService.cntProductByUserStoreId(userStoreId) : storeStockService.cntProductByUserStoreIdAndCategoryId(userStoreId, categoryId);
        List<StoreStockRes> stockList = categoryId == null ? storeStockService.selectAllProductByUserStoreId(userStoreId, paginationParam) : storeStockService.selectProductByUserStoreIdAndCategoryId(userStoreId, categoryId, paginationParam);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stockList", stockList);

        Pagination pagination = new Pagination(count, paginationParam);
        map.put("pagination", pagination);


        return map;

    }


    @PostMapping("/stock/update")
    public String updateStock(@RequestBody List<StockUpdateReq> stockUpdateReq) {
        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        int userStoreId = 2;
        for (StockUpdateReq req : stockUpdateReq) {
            StoreStock storeStock = storeStockService.selectProductByUserStoreIdAndProductId(userStoreId, req.getProductId());
            if (req.getQuantity() != null) {
                storeStock.setQty(req.getQuantity());
            }
            if (req.getPositionId() != null) {
                storeStock.setPositionId(req.getPositionId());
            }
            storeStockService.updateStock(storeStock);
        }
        return "success";
    }

    @GetMapping("/stock/{productName}")
    public List<StoreLocQty> getLocation(@PathVariable String productName, @RequestParam double currentWgs84X, double currentWgs84Y) {
        return storeStockService.getStoreByProductId(productName, currentWgs84X, currentWgs84Y);
    }

    @PostMapping("/stock/dispose")
    public String disposeProduct(@RequestBody List<DisposeReq> list) {
//        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userStoreId = 2;
        for (DisposeReq req : list) {
            StoreStock storeStock = storeStockService.selectProductByUserStoreIdAndProductId(userStoreId, req.getProductId());
            storeStock.setQty(storeStock.getQty() - req.getQty());
            storeStockService.updateStock(storeStock);
            storeStockService.insertDisposeLog(userStoreId, req.getProductId(), req.getQty());
        }
        return "success";
    }

    @GetMapping("/stock/dispose")
    public Map<String, Object> getDisposeLog(PaginationParam paginationParam) {
//        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userStoreId = 2;

        Map<String, Object> map = new HashMap<>();
        int count = storeStockService.getDisposeLogCount(userStoreId);
        List<DisposeLog> disposeLog = storeStockService.getDisposeLog(userStoreId, paginationParam);
        map.put("disposeLog", disposeLog);
        Pagination pagination = new Pagination(count, paginationParam);
        map.put("pagination", pagination);
        return map;
    }

    @GetMapping("/stock/input") // 입고 리스트 확인
    public Map<String, Object> inputList(PaginationParam paginationParam) {
//        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userStoreId = 2;
        List<InputRes> inputList = storeStockService.getOrderListByUserStoreId(userStoreId,paginationParam);
        int count = storeStockService.cntOrderListByUserStoreId(userStoreId);
        Pagination pagination = new Pagination(count, paginationParam);
        Map<String, Object> map = new HashMap<>();
        map.put("inputList", inputList);
        map.put("pagination", pagination);
        return map;
    }

    @GetMapping("/stock/input/{inputId}")
    public List<InputDetailRes> getInputDetail(@PathVariable int inputId) {
        return storeStockService.getOrderDetailByStoreInputId(inputId);
    }

    @PostMapping("/stock/input/{inputId}")
    public void insertStoreStock(@PathVariable int inputId, @RequestBody List<StockInputReq> list) {
//        int userStoreId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userStoreId = 2;
        for (StockInputReq req : list) {
            storeStockService.insertStoreStock(userStoreId, req, inputId);
        }
    }

}

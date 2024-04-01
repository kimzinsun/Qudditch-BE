package com.goldensnitch.qudditch.controller;

import com.goldensnitch.qudditch.dto.*;
import com.goldensnitch.qudditch.dto.storeInput.InputDetailRes;
import com.goldensnitch.qudditch.dto.storeInput.InputRes;
import com.goldensnitch.qudditch.dto.storeInput.StockInputReq;
import com.goldensnitch.qudditch.service.ExtendedUserDetails;
import com.goldensnitch.qudditch.service.StoreStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.io.IOException;
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

    @GetMapping("/stock")
    public ResponseEntity<Map<String, Object>> getStockList(@RequestParam @Nullable Integer categoryId, PaginationParam paginationParam, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userStoreId = userDetails.getId();
            if (userStoreId == null) {
                response.put("status", "fail");
                response.put("message", "로그인이 필요합니다.");
            } else {
                Integer count = categoryId == 0 ? storeStockService.cntProductByUserStoreId(userStoreId, paginationParam.getKeyword()) : storeStockService.cntProductByUserStoreIdAndCategoryId(userStoreId, categoryId, paginationParam.getKeyword());
                List<StoreStockRes> stockList = categoryId == 0 ? storeStockService.selectAllProductByUserStoreId(userStoreId, paginationParam) : storeStockService.selectProductByUserStoreIdAndCategoryId(userStoreId, categoryId, paginationParam);
                if (stockList == null || stockList.isEmpty()) {
                    response.put("status", "fail");
                    response.put("message", "상품이 존재하지 않습니다.");
                } else {
                    response.put("status", "success");
                    response.put("data", stockList);
                    response.put("pagination", new Pagination(count, paginationParam));
                }
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "서버에서 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }



    @PostMapping("/stock/update")
    public ResponseEntity<Map<String, Object>> updateStock(@RequestBody StockUpdateReq stockUpdateReq, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        Integer userStoreId = userDetails.getId();
        if (userStoreId == null) {
            response.put("status", "fail");
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.ok(response);
        }
            StoreStock storeStock = storeStockService.selectProductByUserStoreIdAndProductId(userStoreId, stockUpdateReq.getProductId());
            if (stockUpdateReq.getQuantity() != null) {
                if (stockUpdateReq.getQuantity() < 0) {
                    response.put("status", "fail");
                    response.put("message", "수량은 0 이상이어야 합니다.");
                    return ResponseEntity.ok(response);
                }
                storeStock.setQty(stockUpdateReq.getQuantity());
            }
            if (stockUpdateReq.getPositionId() != null) {
                storeStock.setPositionId(stockUpdateReq.getPositionId());
            }
            storeStockService.updateStock(storeStock);

        response.put("status", "success");
        response.put("message", "재고가 수정되었습니다.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stock/dispose")
    public ResponseEntity<Map<String, Object>> disposeProduct(@RequestBody List<DisposeReq> list, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        Integer userStoreId = userDetails.getId();

        if (userStoreId == null) {
            response.put("status", "fail");
            response.put("message", "로그인이 필요합니다.");
        }
        if (list.isEmpty()) {
            response.put("status", "fail");
            response.put("message", "폐기 상품이 존재하지 않습니다.");
        }
        for (DisposeReq req : list) {
            StoreStock storeStock = storeStockService.selectProductByUserStoreIdAndProductId(userStoreId, req.getProductId());
            if (storeStock == null) {
                response.put("status", "fail");
                response.put("message", "해당 상품이 존재하지 않습니다.");
                response.put("productId", req.getProductId());
            }
            if (storeStock.getQty() < req.getQty()) {
                response.put("status", "fail");
                response.put("message", "폐기할 수량이 재고보다 많습니다.");
            } else {
                storeStock.setQty(storeStock.getQty() - req.getQty());
                storeStockService.updateStock(storeStock);
                storeStockService.insertDisposeLog(userStoreId, req.getProductId(), req.getQty());
                response.put("status", "success");
                response.put("message", "폐기가 완료되었습니다.");
            }
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stock/dispose")
    public ResponseEntity<Map<String, Object>> getDisposeLog(PaginationParam paginationParam, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        Integer userStoreId = userDetails.getId();

        if (userStoreId == null) {
            response.put("status", "fail");
            response.put("message", "로그인이 필요합니다.");
        } else {
            int count = storeStockService.getDisposeLogCount(userStoreId);
            List<DisposeLog> disposeLog = storeStockService.getDisposeLog(userStoreId, paginationParam);
            response.put("data", disposeLog);
            Pagination pagination = new Pagination(count, paginationParam);
            response.put("pagination", pagination);
            response.put("status", "success");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stock/input") // 입고 리스트 확인
    public ResponseEntity<Map<String, Object>> inputList(PaginationParam paginationParam, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        Integer userStoreId = userDetails.getId();
        if (userStoreId == null) {
            response.put("status", "fail");
            response.put("message", "로그인이 필요합니다.");
        } else {
            List<InputRes> inputList = storeStockService.getOrderListByUserStoreId(userStoreId, paginationParam);
            int count = storeStockService.cntOrderListByUserStoreId(userStoreId);
        if (count == 0 || inputList.isEmpty() || inputList == null ){
                response.put("status", "fail");
                response.put("message", "입고 리스트가 존재하지 않습니다.");
            } else {
                Pagination pagination = new Pagination(count, paginationParam);
                response.put("data", inputList);
                response.put("pagination", pagination);
                response.put("status", "success");

            }
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stock/input/download/{inputId}")
    public ResponseEntity<Map<String, Object>> downloadInputList(@PathVariable Integer inputId, @AuthenticationPrincipal ExtendedUserDetails userDetails) throws IOException {
        Map<String, Object> response = new HashMap<>();
        Integer userstoreId = userDetails.getId();
        if (storeStockService.getOrderDetailByStoreInputId(inputId).isEmpty() || storeStockService.getUserStoreIdByInputId(inputId) != userstoreId) {
            response.put("status", "fail");
            response.put("message", "잘못된 접근입니다.");

        } else {
            storeStockService.downloadInputList(inputId);
            response.put("status", "success");
            response.put("message", "입고내역서가 다운로드 되었습니다.");

        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stock/input/{inputId}")
    public ResponseEntity<Map<String, Object>> getInputDetail(@PathVariable Integer inputId, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        Integer userStoreId = userDetails.getId();

        if (userStoreId == null || storeStockService.getUserStoreIdByInputId(inputId) != userStoreId) {
            response.put("status", "fail");
            response.put("message", "잘못된 접근입니다.");
        } else {
            response.put("status", "success");
            List<InputDetailRes> list = storeStockService.getOrderDetailByStoreInputId(inputId);
            response.put("data", list);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stock/input/{inputId}")
    public ResponseEntity<Map<String, Object>> insertStoreStock(@PathVariable int inputId, @RequestBody StockInputReq req, @AuthenticationPrincipal ExtendedUserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        int userStoreId = userDetails.getId();

        if (userStoreId == 0 || storeStockService.getUserStoreIdByInputId(inputId) != userStoreId) {
            response.put("status", "fail");
            response.put("message", "잘못된 접근입니다.");
        } else {

            storeStockService.insertStoreStock(userStoreId, req, inputId);
            response.put("status", "success");
            response.put("message", "입고가 완료되었습니다.");

        }
        return ResponseEntity.ok(response);
    }

}

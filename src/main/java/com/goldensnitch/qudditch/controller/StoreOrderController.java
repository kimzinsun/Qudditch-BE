package com.goldensnitch.qudditch.controller;


import com.goldensnitch.qudditch.dto.StoreOder.OrderDetailWithProducts;
import com.goldensnitch.qudditch.dto.StoreOder.ProductWithDetailQty;
import com.goldensnitch.qudditch.dto.StoreOder.ProductWithQty;
import com.goldensnitch.qudditch.dto.StoreOder.StoreOrderParam;
import com.goldensnitch.qudditch.dto.StoreOrder;
import com.goldensnitch.qudditch.dto.StoreOrderProduct;
import com.goldensnitch.qudditch.service.StoreOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/store/order")
public class StoreOrderController {
    private final StoreOrderService storeOrderService;
    @Autowired
    public StoreOrderController(StoreOrderService storeOrderService) {
        this.storeOrderService = storeOrderService;
    }

    @GetMapping("")
    public Map<String, Object> orderList(StoreOrderParam param) {

        // 제품리스트
        List<StoreOrder> orderList = storeOrderService.orderList(param);
        // 총 수
        int count = storeOrderService.getallList(param);
        // 페이지
        int page = count / 10;
        if(count % 10 > 0) {
            page += 1;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderList", orderList);
        map.put("count", count);
        map.put("page", page);

        return map;
    }

    @PostMapping("")
    public int insertOrder(@RequestBody List<ProductWithQty> products) {
        Integer storeId = 2;

        StoreOrder storeOrder = new StoreOrder();
        storeOrder.setUserStoreId(storeId);
        storeOrderService.insertOrder(storeOrder);

        // storeId 값을 들고와서 변수에 저장
        int orderId = storeOrderService.getStoreId();

        // 제품아이디와 개수를 store_order_product에 저장
        for (ProductWithQty product : products) {
            StoreOrderProduct storeOrderProduct = new StoreOrderProduct();
            storeOrderProduct.setOrderStoreId(orderId);
            storeOrderProduct.setProductId(product.getProductId());
            storeOrderProduct.setQty(product.getQty());

            storeOrderService.insertId(storeOrderProduct);
        }
        return 1;
    }

    @GetMapping("/detail/{id}")
    public OrderDetailWithProducts listDetail(@PathVariable int id) {
        log.info("StoreOrderController.listDetail: {}", id);

        StoreOrder storeOrder = storeOrderService.getStoreOrderById(id);
        ProductWithDetailQty productWithDetailQty = storeOrderService.getProductWithQty(storeOrder.getId());

        return new OrderDetailWithProducts(storeOrder, productWithDetailQty);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadOrderDataAsExcel(@PathVariable int id) {
        try {
            // 제품 정보 가져오기
            StoreOrder storeOrder = storeOrderService.getStoreOrderById(id);
            ProductWithDetailQty productWithDetailQty = storeOrderService.getProductWithQty(storeOrder.getId());

            // 엑셀 파일 생성
            byte[] excelBytes = createExcelFile(storeOrder, productWithDetailQty);

            // 엑셀 파일을 Resource로 래핑
            ByteArrayResource resource = new ByteArrayResource(excelBytes);

            // 다운로드할 때 사용할 파일 이름 설정
            String fileName = "order_data_" + id + ".xlsx";

            // ResponseEntity를 사용하여 파일 다운로드 응답 생성
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);


        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(500).build();
        }
    }

    private byte[] createExcelFile(StoreOrder storeOrder, ProductWithDetailQty productWithDetail) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("발주서");

        // 헤더 추가
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Order ID");
        headerRow.createCell(1).setCellValue("브랜드");
        headerRow.createCell(2).setCellValue("제품명");
        headerRow.createCell(3).setCellValue("수량");


        // 데이터 추가
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(storeOrder.getId());
        dataRow.createCell(1).setCellValue(productWithDetail.getBrand());
        dataRow.createCell(2).setCellValue(productWithDetail.getName());
        dataRow.createCell(3).setCellValue(productWithDetail.getQty());



        // 엑셀 파일을 byte 배열로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    @GetMapping("/detail/update/{id}")
    public int updateOrderProducts(@PathVariable int id, @RequestBody List<ProductWithQty> updateProducts) {

            // 기존 주문 정보
            StoreOrder storeOrder = storeOrderService.getStoreOrderById(id);
            if (storeOrder == null) {
                return 0;
            }

            for (ProductWithQty updatedProduct : updateProducts) {
                StoreOrderProduct storeOrderProduct = new StoreOrderProduct();

                // 주문 및 제품 정보
                storeOrderProduct.setOrderStoreId(storeOrder.getId());
                storeOrderProduct.setProductId(updatedProduct.getProductId());
                storeOrderProduct.setQty(updatedProduct.getQty());

                // 업데이트
               storeOrderService.updateOrderProducts(storeOrderProduct);

            }
            return 1;
    }





}

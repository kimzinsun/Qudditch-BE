package com.goldensnitch.qudditch.controller;


import com.goldensnitch.qudditch.dto.StoreOder.OrderDetailWithProducts;
import com.goldensnitch.qudditch.dto.StoreOder.ProductWithQty;
import com.goldensnitch.qudditch.dto.StoreOder.StoreOrderParam;
import com.goldensnitch.qudditch.dto.StoreOrder;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


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
    public int insertOrder(@RequestBody List<ProductWithQty> order) {
        Integer storeId = 1;
        AtomicReference<Integer> totalAmount = new AtomicReference<>(0);
        List<ProductWithQty> orderProducts = new ArrayList<>();

        StoreOrder storeOrder = new StoreOrder();
        storeOrder.setUserStoreId(storeId);

        for (int i = 0; i < order.size(); i++) {
            ProductWithQty product = order.get(i);
            totalAmount.updateAndGet(v -> v + product.getPrice() * product.getQty());

            // 브랜드와 이름
            String brand = product.getBrand();
            String name = product.getName();

            //  추가작업
            ProductWithQty productWithQty = new ProductWithQty();
            productWithQty.setBrand(brand);
            productWithQty.setName(name);
            orderProducts.add(productWithQty);
            orderProducts.add(product);
        }

        storeOrder.setTotalAmount(totalAmount.get());

        storeOrderService.insertOrder(storeOrder);  // 발주 insert
        // storeOrderService.insertOrderProducts(orderProducts); // 발주 품목 insert (주석 해제하고 사용)

        return 1;
    }



    @GetMapping("/detail/{id}")
    public OrderDetailWithProducts listDetail(@PathVariable int id) {
        log.info("StoreOrderController.listDetail: {}", id);

        StoreOrder storeOrder = storeOrderService.getStoreOrderById(id);
        ProductWithQty productWithQty = storeOrderService.getProductWithQty(storeOrder.getId());

        return new OrderDetailWithProducts(storeOrder, productWithQty);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadOrderDataAsExcel(@PathVariable int id) {
        try {
            // 제품 정보 가져오기
            StoreOrder storeOrder = storeOrderService.getStoreOrderById(id);
            ProductWithQty productWithQty = storeOrderService.getProductWithQty(storeOrder.getId());

            // 엑셀 파일 생성
            byte[] excelBytes = createExcelFile(storeOrder, productWithQty);

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

    private byte[] createExcelFile(StoreOrder storeOrder, ProductWithQty productWithQty) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("발주서");

        // 헤더 추가
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Order ID");
        headerRow.createCell(1).setCellValue("브랜드");
        headerRow.createCell(2).setCellValue("제품명");
        headerRow.createCell(3).setCellValue("수량");
        headerRow.createCell(4).setCellValue("총가격");

        // 데이터 추가
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(storeOrder.getId());
        dataRow.createCell(1).setCellValue(productWithQty.getBrand());
        dataRow.createCell(2).setCellValue(productWithQty.getName());
        dataRow.createCell(3).setCellValue(productWithQty.getQty());
        dataRow.createCell(4).setCellValue(storeOrder.getTotalAmount());

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

    @GetMapping("/update/{id}")
    public int updateOrder(@PathVariable int id, @RequestBody OrderDetailWithProducts orderDetailWithProducts) {
        log.info("StoreOrderController.updateOrder: {}", id);

        // 발주 Update
        StoreOrder setStoreOrder = orderDetailWithProducts.getStoreOrder();
        storeOrderService.updateOrder(setStoreOrder.getId());

        // 제품 Update
        ProductWithQty setProductWithQty = orderDetailWithProducts.getProducts().get(0);
        String brand = setProductWithQty.getBrand();
        String name = setProductWithQty.getName();
        storeOrderService.updateOrderProducts(setProductWithQty.getId());


        return 1;
    }



}

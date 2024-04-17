package com.goldensnitch.qudditch.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelCreator {

    public ResponseEntity<ByteArrayResource> downloadOrderDataAsExcel(String fileName, List<String> Excelheaders, List<List<String>> datas) throws IOException {

        byte[] excelBytes = createExcelFile(Excelheaders, datas);


        ByteArrayResource resource = new ByteArrayResource(excelBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);


        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelBytes.length)
                .body(resource);


    }


    private byte[] createExcelFile(List<String> headers, List<List<String>> datas) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet1");

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }


        for (int i = 0; i < datas.size(); i++) {
            Row dataRow = sheet.createRow(1 + i);
            for (int j = 0; j < headers.size(); j++) {
                dataRow.createCell(j).setCellValue(datas.get(i).get(j));
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }



}

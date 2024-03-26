package com.goldensnitch.qudditch.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelCreator {
    @Value("${excel.file.directory}")
    private String excelFileDirectory;

    public void downloadOrderDataAsExcel(String fileName, List<String> Excelheaders, List<List<String>> datas) throws IOException {

        byte[] excelBytes = createExcelFile(Excelheaders, datas);

        fileName += ".xlsx";
        String filePath = excelFileDirectory + File.separator + fileName;
        saveExcelFile(excelBytes, filePath);

        // 다운로드를 위해 파일을 준비합니다.
        ByteArrayResource resource = new ByteArrayResource(excelBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);


        ResponseEntity.ok()
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

    private void saveExcelFile(byte[] excelBytes, String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(excelBytes);
        fos.close();
    }


}

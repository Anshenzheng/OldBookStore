package com.oldbookstore.service;

import com.oldbookstore.dto.ProductDTO;
import com.oldbookstore.dto.UserDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {
    
    public byte[] exportProductsToExcel(List<ProductDTO> products) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("商品列表");
            
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "标题", "价格", "分类", "卖家", "状态", "审核状态", "浏览量", "创建时间"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            int rowNum = 1;
            for (ProductDTO product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId() != null ? product.getId() : 0);
                row.createCell(1).setCellValue(product.getTitle() != null ? product.getTitle() : "");
                row.createCell(2).setCellValue(product.getPrice() != null ? product.getPrice().doubleValue() : 0);
                row.createCell(3).setCellValue(product.getCategoryName() != null ? product.getCategoryName() : "");
                row.createCell(4).setCellValue(product.getSellerNickname() != null ? product.getSellerNickname() : "");
                row.createCell(5).setCellValue(product.getStatus() != null ? product.getStatus() : "");
                row.createCell(6).setCellValue(product.getAuditStatus() != null ? product.getAuditStatus() : "");
                row.createCell(7).setCellValue(product.getViewCount() != null ? product.getViewCount() : 0);
                row.createCell(8).setCellValue(product.getCreatedAt() != null ? product.getCreatedAt().toString() : "");
            }
            
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
    
    public byte[] exportUsersToExcel(List<UserDTO> users) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("用户列表");
            
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "用户名", "昵称", "邮箱", "手机号", "角色", "状态", "注册时间", "最后登录"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            int rowNum = 1;
            for (UserDTO user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId() != null ? user.getId() : 0);
                row.createCell(1).setCellValue(user.getUsername() != null ? user.getUsername() : "");
                row.createCell(2).setCellValue(user.getNickname() != null ? user.getNickname() : "");
                row.createCell(3).setCellValue(user.getEmail() != null ? user.getEmail() : "");
                row.createCell(4).setCellValue(user.getPhone() != null ? user.getPhone() : "");
                row.createCell(5).setCellValue(user.getRole() != null ? user.getRole() : "");
                row.createCell(6).setCellValue(user.getStatus() != null ? user.getStatus() : "");
                row.createCell(7).setCellValue(user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
                row.createCell(8).setCellValue(user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : "");
            }
            
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }
}

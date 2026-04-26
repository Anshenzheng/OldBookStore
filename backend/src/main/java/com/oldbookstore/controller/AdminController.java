package com.oldbookstore.controller;

import com.oldbookstore.dto.ApiResponse;
import com.oldbookstore.dto.ProductDTO;
import com.oldbookstore.dto.UserDTO;
import com.oldbookstore.entity.Category;
import com.oldbookstore.entity.User;
import com.oldbookstore.security.UserPrincipal;
import com.oldbookstore.service.AdminService;
import com.oldbookstore.service.CategoryService;
import com.oldbookstore.service.ExcelExportService;
import com.oldbookstore.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private StatisticsService statisticsService;
    
    @Autowired
    private ExcelExportService excelExportService;
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = statisticsService.getDashboardStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/products/pending")
    public ResponseEntity<Page<ProductDTO>> getPendingProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> products = adminService.getPendingProducts(page, size);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/products")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> products = adminService.getAllProducts(page, size);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping("/products/{id}/approve")
    public ResponseEntity<?> approveProduct(@PathVariable Long id) {
        try {
            ProductDTO product = adminService.approveProduct(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "审核失败: " + e.getMessage()));
        }
    }
    
    @PostMapping("/products/{id}/reject")
    public ResponseEntity<?> rejectProduct(@PathVariable Long id, 
                                           @RequestBody(required = false) Map<String, String> body) {
        try {
            String reason = body != null ? body.get("reason") : "审核不通过";
            ProductDTO product = adminService.rejectProduct(id, reason);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "审核失败: " + e.getMessage()));
        }
    }
    
    @PostMapping("/products/{id}/off-shelf")
    public ResponseEntity<?> takeProductOffShelf(@PathVariable Long id) {
        try {
            ProductDTO product = adminService.takeProductOffShelf(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "下架失败: " + e.getMessage()));
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<User> users = adminService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping("/users/{id}/ban")
    public ResponseEntity<?> banUser(@PathVariable Long id) {
        try {
            User user = adminService.banUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "禁用用户失败: " + e.getMessage()));
        }
    }
    
    @PostMapping("/users/{id}/unban")
    public ResponseEntity<?> unbanUser(@PathVariable Long id) {
        try {
            User user = adminService.unbanUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "解禁用户失败: " + e.getMessage()));
        }
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category savedCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "创建分类失败: " + e.getMessage()));
        }
    }
    
    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "更新分类失败: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse(true, "分类已删除"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "删除分类失败: " + e.getMessage()));
        }
    }
    
    @GetMapping("/export/products")
    public ResponseEntity<byte[]> exportProducts() {
        try {
            Page<ProductDTO> productPage = adminService.getAllProducts(0, 1000);
            List<ProductDTO> products = productPage.getContent();
            
            byte[] excelBytes = excelExportService.exportProductsToExcel(products);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                    new String("products.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/export/users")
    public ResponseEntity<byte[]> exportUsers() {
        try {
            Page<User> userPage = adminService.getAllUsers(0, 1000);
            List<UserDTO> userDTOs = new ArrayList<>();
            
            for (User user : userPage.getContent()) {
                UserDTO dto = new UserDTO();
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                dto.setNickname(user.getNickname());
                dto.setEmail(user.getEmail());
                dto.setPhone(user.getPhone());
                dto.setAvatar(user.getAvatar());
                dto.setRole(user.getRole().name());
                dto.setStatus(user.getStatus().name());
                dto.setCreatedAt(user.getCreatedAt());
                dto.setLastLoginAt(user.getLastLoginAt());
                userDTOs.add(dto);
            }
            
            byte[] excelBytes = excelExportService.exportUsersToExcel(userDTOs);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                    new String("users.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

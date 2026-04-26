package com.oldbookstore.controller;

import com.oldbookstore.dto.ApiResponse;
import com.oldbookstore.dto.CreateProductRequest;
import com.oldbookstore.dto.ProductDTO;
import com.oldbookstore.security.UserPrincipal;
import com.oldbookstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/public/list")
    public ResponseEntity<Page<ProductDTO>> getApprovedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> products = productService.getApprovedProducts(page, size);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/public/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> products = productService.searchProducts(keyword, page, size);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> products = productService.getProductsByCategory(categoryId, page, size);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/public/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Page<ProductDTO>> getMyProducts(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> products = productService.getMyProducts(userPrincipal.getId(), page, size);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> createProduct(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateProductRequest request) {
        try {
            ProductDTO product = productService.createProduct(userPrincipal.getId(), request);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "创建商品失败: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> updateProduct(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request) {
        try {
            ProductDTO product = productService.updateProduct(userPrincipal.getId(), id, request);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "更新商品失败: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> deleteProduct(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id) {
        try {
            productService.deleteProduct(userPrincipal.getId(), id);
            return ResponseEntity.ok(new ApiResponse(true, "商品已删除"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "删除商品失败: " + e.getMessage()));
        }
    }
}

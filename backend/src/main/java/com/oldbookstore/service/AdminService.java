package com.oldbookstore.service;

import com.oldbookstore.dto.ProductDTO;
import com.oldbookstore.entity.Product;
import com.oldbookstore.entity.User;
import com.oldbookstore.repository.ProductRepository;
import com.oldbookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdminService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Page<ProductDTO> getPendingProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Product> products = productRepository.findByAuditStatusOrderByCreatedAtDesc(
                Product.AuditStatus.PENDING, pageable);
        return products.map(this::convertToDTO);
    }
    
    @Transactional
    public ProductDTO approveProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setAuditStatus(Product.AuditStatus.APPROVED);
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    @Transactional
    public ProductDTO rejectProduct(Long productId, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setAuditStatus(Product.AuditStatus.REJECTED);
        product.setRejectReason(reason);
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    @Transactional
    public ProductDTO takeProductOffShelf(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setStatus(Product.ProductStatus.OFF_SHELF);
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::convertToDTO);
    }
    
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findAll(pageable);
    }
    
    @Transactional
    public User banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(User.UserStatus.BANNED);
        return userRepository.save(user);
    }
    
    @Transactional
    public User unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(User.UserStatus.ACTIVE);
        return userRepository.save(user);
    }
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImages(product.getImages());
        dto.setStatus(product.getStatus().name());
        dto.setAuditStatus(product.getAuditStatus().name());
        dto.setRejectReason(product.getRejectReason());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setViewCount(product.getViewCount());
        dto.setContactInfo(product.getContactInfo());
        
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        
        if (product.getSeller() != null) {
            dto.setSellerId(product.getSeller().getId());
            dto.setSellerNickname(product.getSeller().getNickname());
            dto.setSellerAvatar(product.getSeller().getAvatar());
        }
        
        return dto;
    }
}

package com.oldbookstore.service;

import com.oldbookstore.dto.CreateProductRequest;
import com.oldbookstore.dto.ProductDTO;
import com.oldbookstore.entity.Category;
import com.oldbookstore.entity.Product;
import com.oldbookstore.entity.User;
import com.oldbookstore.repository.CategoryRepository;
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
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Page<ProductDTO> getApprovedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findByAuditStatusAndStatusOrderByCreatedAtDesc(
                Product.AuditStatus.APPROVED, Product.ProductStatus.ON_SALE, pageable);
        return products.map(this::convertToDTO);
    }
    
    public Page<ProductDTO> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.searchApprovedProducts(keyword, pageable);
        return products.map(this::convertToDTO);
    }
    
    public Page<ProductDTO> getProductsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findByCategoryIdAndApproved(categoryId, pageable);
        return products.map(this::convertToDTO);
    }
    
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        
        return convertToDTO(product);
    }
    
    public Page<ProductDTO> getMyProducts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findBySellerIdOrderByCreatedAtDesc(userId, pageable);
        return products.map(this::convertToDTO);
    }
    
    @Transactional
    public ProductDTO createProduct(Long userId, CreateProductRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImages(request.getImages());
        product.setContactInfo(request.getContactInfo());
        product.setSeller(user);
        
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        product.setAuditStatus(Product.AuditStatus.PENDING);
        product.setStatus(Product.ProductStatus.ON_SALE);
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    @Transactional
    public ProductDTO updateProduct(Long userId, Long productId, CreateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (!product.getSeller().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this product");
        }
        
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImages(request.getImages());
        product.setContactInfo(request.getContactInfo());
        product.setUpdatedAt(LocalDateTime.now());
        product.setAuditStatus(Product.AuditStatus.PENDING);
        
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }
    
    @Transactional
    public void deleteProduct(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (!product.getSeller().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this product");
        }
        
        productRepository.delete(product);
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

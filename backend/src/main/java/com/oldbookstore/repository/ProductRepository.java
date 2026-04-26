package com.oldbookstore.repository;

import com.oldbookstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    Page<Product> findByAuditStatusOrderByCreatedAtDesc(Product.AuditStatus auditStatus, Pageable pageable);
    
    Page<Product> findByAuditStatusAndStatusOrderByCreatedAtDesc(
            Product.AuditStatus auditStatus, 
            Product.ProductStatus status, 
            Pageable pageable);
    
    Page<Product> findBySellerIdOrderByCreatedAtDesc(Long sellerId, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.auditStatus = 'APPROVED' AND p.status = 'ON_SALE' " +
           "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchApprovedProducts(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.auditStatus = 'APPROVED' AND p.status = 'ON_SALE' " +
           "AND p.category.id = :categoryId")
    Page<Product> findByCategoryIdAndApproved(@Param("categoryId") Long categoryId, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.auditStatus = :status")
    long countByAuditStatus(@Param("status") Product.AuditStatus status);
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.status = :status")
    long countByStatus(@Param("status") Product.ProductStatus status);
}

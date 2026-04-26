package com.oldbookstore.service;

import com.oldbookstore.entity.Product;
import com.oldbookstore.entity.Report;
import com.oldbookstore.repository.ProductRepository;
import com.oldbookstore.repository.ReportRepository;
import com.oldbookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReportRepository reportRepository;
    
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        long pendingProducts = productRepository.countByAuditStatus(Product.AuditStatus.PENDING);
        long approvedProducts = productRepository.countByAuditStatus(Product.AuditStatus.APPROVED);
        long soldProducts = productRepository.countByStatus(Product.ProductStatus.SOLD);
        long pendingReports = reportRepository.count((root, query, cb) -> 
                cb.equal(root.get("status"), Report.ReportStatus.PENDING));
        
        stats.put("totalUsers", totalUsers);
        stats.put("totalProducts", totalProducts);
        stats.put("pendingProducts", pendingProducts);
        stats.put("approvedProducts", approvedProducts);
        stats.put("soldProducts", soldProducts);
        stats.put("pendingReports", pendingReports);
        
        return stats;
    }
}

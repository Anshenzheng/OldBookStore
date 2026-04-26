package com.oldbookstore.config;

import com.oldbookstore.entity.Category;
import com.oldbookstore.entity.User;
import com.oldbookstore.repository.CategoryRepository;
import com.oldbookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNickname("管理员");
            admin.setEmail("admin@example.com");
            admin.setRole(User.Role.ADMIN);
            admin.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(admin);
            System.out.println("默认管理员账户已创建: admin / admin123");
        }
        
        if (categoryRepository.count() == 0) {
            createCategory("二手书籍", "教材、小说、工具书等", "📚", 1);
            createCategory("数码产品", "手机、电脑、平板等", "💻", 2);
            createCategory("生活用品", "宿舍用品、小家电等", "🏠", 3);
            createCategory("服饰鞋包", "衣服、鞋子、包包等", "👕", 4);
            createCategory("运动户外", "运动器材、户外用品等", "⚽", 5);
            createCategory("其他闲置", "其他二手物品", "🎁", 6);
            System.out.println("默认分类已创建");
        }
    }
    
    private void createCategory(String name, String description, String icon, int sortOrder) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setIcon(icon);
        category.setSortOrder(sortOrder);
        categoryRepository.save(category);
    }
}

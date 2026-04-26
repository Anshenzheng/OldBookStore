# 校园二手书和闲置物品交易平台

## 项目简介

这是一个面向校园的二手书和闲置物品交易平台，采用前后端分离架构，界面风格简洁清新，偏向青春校园风。

### 功能特性

**学生端功能：**
- 用户注册和登录
- 发布闲置物品
- 搜索和浏览商品
- 查看商品详情
- 联系卖家
- 管理自己发布的商品

**管理员端功能：**
- 商品审核（审核通过后才能显示）
- 商品管理（下架等操作）
- 用户管理（禁用/解禁用户）
- 数据统计（用户数、商品数、待审核数等）
- 数据导出（Excel格式）

**通用功能：**
- 所有列表分页显示
- JWT安全认证
- 角色权限控制
- 商品分类导航

---

## 技术栈

**后端：**
- Java 11
- Spring Boot 2.7.18
- Spring Security + JWT
- Spring Data JPA
- MySQL 8.0
- Apache POI (Excel导出)

**前端：**
- Angular 15
- TypeScript
- Bootstrap 5
- RxJS

---

## 环境要求

- JDK 11 或更高版本
- Node.js 16.x 或更高版本
- MySQL 8.0
- Maven 3.6+
- npm 或 yarn

---

## 安装与启动

### 第一步：准备数据库

1. 登录MySQL，创建数据库：
```sql
CREATE DATABASE old_book_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改后端配置文件 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/old_book_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: your_username    # 替换为你的MySQL用户名
    password: your_password    # 替换为你的MySQL密码
```

### 第二步：启动后端服务

1. 进入后端目录：
```bash
cd backend
```

2. 使用Maven构建项目：
```bash
mvn clean install
```

3. 运行应用：
```bash
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

**注意：** 首次启动时，系统会自动创建数据库表，并初始化以下数据：
- 管理员账户：用户名 `admin`，密码 `admin123`
- 6个默认分类：二手书籍、数码产品、生活用品、服饰鞋包、运动户外、其他闲置

### 第三步：启动前端服务

1. 新开一个终端，进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm start
```

前端应用将在 `http://localhost:4200` 启动。

---

## 验证步骤

### 1. 访问应用

打开浏览器，访问 `http://localhost:4200`

### 2. 管理员登录测试

1. 点击导航栏的「登录」
2. 使用管理员账户登录：
   - 用户名：`admin`
   - 密码：`admin123`
3. 登录成功后，导航栏会显示「管理后台」
4. 点击「管理后台」进入管理界面：
   - 数据概览：查看统计数据
   - 商品审核：查看待审核商品
   - 商品管理：管理所有商品
   - 用户管理：管理用户
   - 数据导出：导出Excel

### 3. 学生用户注册与登录测试

1. 退出登录（点击右上角用户名 → 退出登录）
2. 点击「注册」
3. 填写注册信息：
   - 用户名：如 `student001`
   - 邮箱：如 `student001@example.com`
   - 密码：至少6位
   - 昵称：如「测试学生」
4. 点击「注册」，注册成功后跳转到登录页
5. 使用新注册的账户登录

### 4. 发布商品测试

1. 登录学生账户后
2. 点击「发布闲置」
3. 填写商品信息：
   - 商品标题：如「二手教材 - 高数上册」
   - 商品描述：如「九成新，无笔记」
   - 价格：如 `25.00`
   - 商品分类：选择「二手书籍」
   - 商品图片：可填写图片URL（多个用逗号分隔）
   - 联系方式：如手机号或微信
4. 点击「发布商品」
5. 提示：商品发布成功，等待管理员审核

### 5. 商品审核流程测试

1. 退出学生账户
2. 登录管理员账户（admin / admin123）
3. 进入「管理后台」→「商品审核」
4. 可以看到刚发布的待审核商品
5. 点击「通过」或「拒绝」
6. 审核通过后，商品会在首页和商品列表显示

### 6. 商品浏览测试

1. 退出管理员账户或重新打开一个浏览器窗口
2. 无需登录即可浏览：
   - 首页：显示最新发布的商品
   - 商品列表：按分类筛选、搜索
   - 商品详情：查看详细信息
3. 登录后可以：
   - 发布商品
   - 查看「我的商品」
   - 联系卖家

### 7. 分页功能验证

在商品列表页、我的商品页、管理后台的列表页，都会显示分页控件，可以切换页面查看更多数据。

---

## API接口说明

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 公开接口（无需登录）
- `GET /api/categories` - 获取所有分类
- `GET /api/products/public/list` - 获取已审核商品列表
- `GET /api/products/public/search` - 搜索商品
- `GET /api/products/public/category/{id}` - 按分类获取商品
- `GET /api/products/public/{id}` - 获取商品详情

### 学生接口（需要登录，角色为STUDENT）
- `GET /api/products/my` - 获取我的商品
- `POST /api/products` - 发布商品
- `PUT /api/products/{id}` - 更新商品
- `DELETE /api/products/{id}` - 删除商品

### 管理员接口（需要登录，角色为ADMIN）
- `GET /api/admin/statistics` - 获取统计数据
- `GET /api/admin/products/pending` - 获取待审核商品
- `GET /api/admin/products` - 获取所有商品
- `POST /api/admin/products/{id}/approve` - 审核通过
- `POST /api/admin/products/{id}/reject` - 审核拒绝
- `POST /api/admin/products/{id}/off-shelf` - 下架商品
- `GET /api/admin/users` - 获取用户列表
- `POST /api/admin/users/{id}/ban` - 禁用用户
- `POST /api/admin/users/{id}/unban` - 解禁用户
- `GET /api/admin/export/products` - 导出商品Excel
- `GET /api/admin/export/users` - 导出用户Excel

---

## 项目结构

```
OldBookStore/
├── backend/                    # 后端Spring Boot项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/oldbookstore/
│   │   │   │   ├── config/          # 配置类
│   │   │   │   ├── controller/      # 控制器
│   │   │   │   ├── dto/             # 数据传输对象
│   │   │   │   ├── entity/          # 实体类
│   │   │   │   ├── repository/      # 数据访问层
│   │   │   │   ├── security/        # 安全配置
│   │   │   │   └── service/         # 业务逻辑层
│   │   │   └── resources/
│   │   │       └── application.yml  # 应用配置
│   └── pom.xml                      # Maven配置
│
└── frontend/                   # 前端Angular项目
    ├── src/
    │   ├── app/
    │   │   ├── components/          # 组件
    │   │   ├── guards/              # 路由守卫
    │   │   ├── interceptors/        # 拦截器
    │   │   ├── models/              # 模型定义
    │   │   └── services/            # 服务
    │   ├── index.html
    │   ├── main.ts
    │   └── styles.css
    ├── angular.json                 # Angular配置
    ├── package.json                 # NPM配置
    └── tsconfig.json                # TypeScript配置
```

---

## 常见问题

### 1. 数据库连接失败
- 检查MySQL服务是否启动
- 确认数据库名称、用户名、密码配置正确
- 检查MySQL是否允许远程连接

### 2. 前端无法访问后端API
- 确认后端服务已启动（端口8080）
- 检查浏览器控制台是否有CORS错误
- 确认前后端都在本地运行（localhost）

### 3. 管理员账户无法登录
- 确认数据库已初始化（首次启动后端会自动初始化）
- 检查用户名密码：admin / admin123

### 4. 商品发布后不显示
- 商品需要管理员审核通过后才会显示
- 登录管理员账户，进入管理后台审核商品

---

## 开发说明

### 后端开发
- 使用IntelliJ IDEA打开 `backend` 目录
- 确保JDK 11已配置
- 运行 `OldBookStoreApplication` 主类

### 前端开发
- 使用VS Code打开 `frontend` 目录
- 安装依赖：`npm install`
- 启动开发服务器：`npm start`
- 代码修改后会自动热重载

---

## 许可证

本项目仅供学习交流使用。

# hsbc

# Banking Transaction Manager

一个基于 Spring Boot 的轻量级交易管理系统，支持创建、查询、更新和删除交易记录。

---

## 📦 项目简介

本项目提供了一个基础的银行交易管理服务，包含以下核心功能：

- 创建交易（POST）
- 分页获取所有交易（GET）
- 根据 ID 获取单个交易（GET）
- 更新交易（PUT）
- 删除交易（DELETE）

适用于学习 REST API 开发、Spring Boot 基础架构以及构建简单的后端服务。

> ✅ 当前版本为 **无数据库、无缓存、纯内存实现**，适合本地测试与教学用途。如需扩展可轻松接入数据库或缓存系统。

---

## 🧰 技术栈

| 技术/框架        | 版本         | 用途说明 |
|------------------|--------------|----------|
| Java             | JDK 17+      | 基础语言环境 |
| Spring Boot      | 3.1.0        | 快速构建 Web 应用程序 |
| Maven            | 3.x+         | 依赖管理工具 |
| JUnit 5          | 内建         | 单元测试 |

> ⚠️ 注意：本项目目前不依赖任何数据库或缓存中间件，数据保存在内存中，重启后会丢失。

---

## 📁 项目结构概览

```
src
├── main
│   ├── java/com.banking
│   │   ├── controller/TransactionController.java    // 控制器层，处理 HTTP 请求
│   │   ├── service/TransactionService.java          // 业务逻辑层
│   │   ├── model/Transaction.java                   // 数据模型类
│   │   ├── exception/GlobalExceptionHandler.java    // 异常处理类
│   │   └── dto/ApiResponse.java                     // 统一响应封装类
│   └── resources
│       └── application.properties                   // 配置文件
└── test
    └── java/com.banking.controller/TransactionControllerTest.java  // 接口测试类
```

---

## 🔧 快速启动

### 1. 安装依赖

确保你已安装以下工具：

- JDK 21+
- Maven 3.x+

然后运行：

```bash
mvn clean install
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

默认启动在 `http://localhost:8080`

---

## 🌐 API 文档

完整 API 列表请参考：[API 文档](#api-文档)

---

## 🧪 测试说明

我们使用 `MockMvc` 进行接口测试，验证每个端点的功能是否正常。

运行测试命令：

```bash
mvn test
```

---

## ✅ 已实现特性

| 功能               | 状态 |
|--------------------|------|
| 创建交易           | ✅   |
| 分页获取交易列表   | ✅   |
| 获取指定ID的交易   | ✅   |
| 更新交易           | ✅   |
| 删除交易           | ✅   |
| 统一响应格式封装   | ✅   |
| 接口单元测试       | ✅   |

---

## 📝 示例请求与响应

### 创建交易（POST）

**Request:**
```http
POST /api/transactions HTTP/1.1
Content-Type: application/json

{
  "type": "Deposit",
  "amount": 100.0
}
```

**Response:**
```json
{
  "success": true,
  "message": "Transaction created successfully",
  "data": {
    "id": 1,
    "type": "Deposit",
    "amount": 100.0
  },
  "errors": {}
}
```

### 获取交易列表（GET）

**Request:**
```http
GET /api/transactions?page=0&size=10 HTTP/1.1
Accept: application/json
```

**Response:**
```json
{
  "success": true,
  "message": "Transactions fetched successfully",
  "data": [
    {
      "id": 1,
      "type": "Deposit",
      "amount": 100.0
    }
  ],
  "errors": {}
}
```

---

## 📎 未来可扩展方向

- 支持数据库持久化（如 MySQL ）
- 添加用户认证模块（如 JWT）
- 引入日志追踪（如 Logback + MDC）
- 使用 Swagger 自动生成 API 文档
- 实现缓存机制（如 Redis、Spring Cache）


---

## 📬 联系方式

如果你有任何问题、建议或想贡献代码，请通过以下方式联系我：

- 邮箱：344983176@qq.com

---


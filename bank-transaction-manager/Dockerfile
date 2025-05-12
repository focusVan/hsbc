# 使用支持 Java 21 的基础镜像（例如 Eclipse Temurin）
FROM eclipse-temurin:21-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将 Maven 构建脚本和源码复制到容器中
COPY . /app

# 安装 Maven（如果系统没有自带）
RUN apk add --no-cache maven

# 执行 Maven 构建命令（跳过测试）
RUN mvn clean package -DskipTests

# 暴露应用监听的端口（默认为8080）
EXPOSE 8080

# 可选：设置环境变量，如 Spring Active Profile
ENV SPRING_PROFILES_ACTIVE=prod

# 启动 Spring Boot 应用
CMD ["java", "--enable-preview", "-jar", "target/banking-transaction-manager-1.0-SNAPSHOT.jar"]
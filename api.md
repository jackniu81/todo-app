Spring Boot入门实战，实现一个TODO系统的后端API项目。

# 实现功能
1. 基于Spring Boot框架，实现一个TODO程序的后端API微服务。
2. 使用MySQL数据库保存数据
3. 使用JpaRepository访问数据库
4. 基于Swagger生成在线API手册

# 前期准备
1. 安装Maven
2. 安装Java JDK 
3. 开发工具（IDE），推荐 IntelliJ IDEA

> 环境： Java V8; Spring Boot 2.4.

# 新建项目
## 1 基于Spring Initializr初始化项目
访问https://start.spring.io 新建一个Spring Boot的项目，Dependencies中选中Spring Web。下载到本地，并使用IntelliJ IDEA打开。

## 添加依赖
```xml
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-boot-starter</artifactId>
			<version>3.0.0</version>
		</dependency>
		<dependency>
			<groupId>javax.validation</groupId>
			<artifactId>validation-api</artifactId>
			<version>2.0.1.Final</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
```
说明：

## 3 增加配置文件
1） 创建`config`文件夹在项目根目录（与 src 目录平级）。 (也可以放在 src/main/resources 下面）
2） 新建 application.yaml 或 application.properties 文件。
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todoapi
    username: root
    password: your_db_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    properties:
      hibernate:
        hbm2ddl:
          auto: update
```
说明：
1. server.port指定api运行端口
2. spring.datasource 数据库连接配置
3. spring.jpa.properties.hibernate.hbm2ddl.auto 根据Java中的类，自动创建数据库表结构。

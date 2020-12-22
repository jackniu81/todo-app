Spring Boot 入门实战，实现一个 TODO 系统的后端 API 项目。

- [1. 实现功能](#1-----)
- [2. 前期准备](#2-----)
- [3. 新建项目](#3-----)
  * [3.1. 基于 Spring Initializr 初始化项目](#31----spring-initializr------)
  * [3.2. 添加依赖](#32-----)
  * [3.3. 增加配置文件](#33-------)
- [4. 编写代码实现TODO程序的API](#4-------todo---api)
  * [4.1. 实体类-Task](#41-----task)
  * [4.2. 数据库访问](#42------)
  * [4.3. API代码。](#43-api---)
  * [4.4. 应用主类增加注解@EnableOpenApi](#44----------enableopenapi)
- [5. 执行API](#5---api)
- [6. 总结](#6---)

# 1. 实现功能

1. 基于 Spring Boot 框架，实现一个 TODO 程序的后端 API 微服务。
2. 使用 MySQL 数据库保存数据
3. 使用 JpaRepository 访问数据库
4. 基于 Swagger 生成在线 API 手册

# 2. 前期准备

1. 安装 Maven
2. 安装 Java JDK
3. 开发工具（IDE），推荐 IntelliJ IDEA

> 环境： Java V8; Spring Boot 2.4.

# 3. 新建项目

## 3.1. 基于 Spring Initializr 初始化项目

访问https://start.spring.io 新建一个 Spring Boot 的项目，Dependencies 中选中 Spring Web。下载到本地，并使用 IntelliJ IDEA 打开。

## 3.2. 添加依赖

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
* lombok: 帮你封装好多实用方法,比如get,set和log方法，免去写大量的GET,SET方法。这样既少写代码，又代码整洁。
* springfox-boot-starter: 基于wagger，开源的API doc框架,可以将我们的Controller的方法以文档的形式展现。并且可以在线测试api。
* validation-api: API端校验接收到的参数。
* mysql-connector-java：mySql连接库。
* spring-boot-starter-data-jpa: JpaRepository相关,ORM库，用于数据库访问。

## 3.3. 增加配置文件

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

1. server.port 指定 api 运行端口
2. spring.datasource 数据库连接配置
3. spring.jpa.properties.hibernate.hbm2ddl.auto 根据 Java 中的类，自动创建数据库表结构。

# 4. 编写代码实现TODO程序的API
## 4.1. 实体类-Task
```java
@NoArgsConstructor
@Data
@Entity
@ApiModel("Task")
public class Task {

    @Id
    @GeneratedValue
    @ApiModelProperty("Primary Key - ID")
    private Long id;

    @ApiModelProperty("Content of the task")
    @Size(max = 20)
    private String content;

    @ApiModelProperty("Completed or not")
    private boolean completed;

    @ApiModelProperty("Last update time")
    private Date editTime;
}
```
说明：
1. @ApiModel， @ApiModelProperty 注解用于生成Wagger文档的描述；
2. @NoArgsConstructor， @Data 来自lombok，用于自动生成get/set方法；
3. @Size 自动校验API参数，并自动体现在生成Wagger的文档中
4. @Entity, @Id, @GeneratedValue Jpa相关，用于ORM等

## 4.2. 数据库访问
因为我们采用了JpaRepository，它本身就包含了Hibernate的ORM相关实现， 所以默认提供了fingAll(),findAllById()，save(), Update()等实现。这里我们在内置实现的基础上，增加一个findByCompleted方法。
```java
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(boolean completed);
}
```
是不是还需要再写代码来实现这个方法啊？错了，不需要，因为我们已经实现了。这就是Spring-data-jpa的一大特性：`通过解析方法名创建查询`。参数中的参数`complete`自动对应Task类中的`complete`。

## 4.3. API代码。
创建TaskController.java
```java
@Api(tags="Task Management")
@RequestMapping("/todo-api/v1/tasks")
@RestController
public class TaskController {
    @Autowired
    TaskRepository taskRepository;

    @ApiOperation("Get task list ")
    @GetMapping("/")
    public List<Task> index(@ApiParam("filter by completed. Empty means all") @RequestParam(required = false) Boolean completed) {

        return completed == null?
                taskRepository.findAll():
                taskRepository.findByCompleted(completed);
    }

    @ApiOperation("Create a new task")
    @PostMapping("/")
    public String postTask(@RequestBody Task task) {
        task.setEditTime(new Date());
        this.taskRepository.save(task);
        return "success";
    }

    @ApiOperation("Update a task")
    @PutMapping("/{id}")
    public String putTask(@PathVariable Long id, @RequestBody Task newTask) {
        newTask.setEditTime(new Date());
        this.taskRepository.save((newTask));
        return "success";
    }
```
说明：
1. @ApiOperation, @Api 注解用于生成Wagger文档的描述；
2. @RequestMapping，@GetMapping， @PostMapping， @PutMapping 用于指定API路由
3. @RequestBody， @PathVariable，@RequestParam 用于接收API调用时传入的参数。 （分别对应 body，url路径，url查询中的参数）
4. @Autowired， TaskRepository taskRepository; 注入TaskRepository，进行数据库操作。

## 4.4. 应用主类增加注解@EnableOpenApi
增加注解@EnableOpenApi, 用于启用Swagger在线文档支持。
```java
@EnableOpenApi
@SpringBootApplication
public class TodoApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoApiApplication.class, args);
	}
}
```

# 5. 执行API
如果使用IntelliJ IDEA ，直接找到TodoApiApplication文件，右键，选择 'Run ...' 即可。
> http://localhost:8080/swagger-ui/, 可以直接查看、测试实现的api。 

![](https://raw.githubusercontent.com/jackniu81/todo-app/main/img/todo_list.png)

测试api，点击'Try it Out'即可。然后可以修改传入参数

![](https://raw.githubusercontent.com/jackniu81/todo-app/main/img/todo_try.png)

# 6. 总结
麻雀虽小，五脏俱全。这个小的API包含了数据库初始化，数据库访问，Swagger文档等内容。但是代码量确实很小的，可以看出Spring Boot框架的强大。Spring Boot强调开箱即用，提供各种默认配置来简化项目配置。基于`约定优于配置`的原则，简化配置，实现快速开发。

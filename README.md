# springcloud-demo
# SpringCloud 



## 1 单体应用

### 1.1项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614112343912-1529392064.png)

### 1.2配置文件

- application.yml

  ```yml
  server:
    port: 8080
    servlet:
      context-path: /
  
  
  spring:
    thymeleaf:
      mode: HTML5
      encoding: utf-8
      servlet:
        content-type: text/html
      cache: false
  ```

- pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.tzy</groupId>
      <artifactId>testcloud</artifactId>
      <version>1.0-SNAPSHOT</version>
      <packaging>war</packaging>
  
      <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.0.3.RELEASE</version>
          <relativePath/>
      </parent>
  
      <properties>
          <java.version>1.8</java.version>
      </properties>
  
      <dependencies>
          <!--lombok-->
          <dependency>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <optional>true</optional>
          </dependency>
  
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter</artifactId>
          </dependency>
  
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
  
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-thymeleaf</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-devtools</artifactId>
              <optional>true</optional> <!-- 这个需要为 true 热部署才有效 -->
          </dependency>
          <dependency>
              <groupId>cn.hutool</groupId>
              <artifactId>hutool-all</artifactId>
              <version>4.3.1</version>
          </dependency>
      </dependencies>
  
      <build>
          <plugins>
              <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
              </plugin>
              <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-war-plugin</artifactId>
                  <configuration>
                      <failOnMissingWebXml>false</failOnMissingWebXml>
                  </configuration>
              </plugin>
          </plugins>
      </build>
      
  </project>
  ```

  

### 1.3页面

- products.html

  ```html
  <!DOCTYPE HTML>
  <html xmlns:th="http://www.thymeleaf.org">
  <head>
      <title>products</title>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      <style>
          table {
              border-collapse: collapse;
              width: 400px;
              margin: 20px auto;
          }
  
          td, th {
              border: 1px solid gray;
          }
      </style>
  </head>
  <body>
  <div class="workingArea">
      <table>
          <thead>
          <tr>
              <th>id</th>
              <th>产品名称</th>
              <th>价格</th>
          </tr>
          </thead>
          <tbody>
          <tr th:each="p: ${ps}">
              <td th:text="${p.id}"></td>
              <td th:text="${p.name}"></td>
              <td th:text="${p.price}"></td>
          </tr>
          </tbody>
      </table>
  </div>
  </body>
  </html>
  ```

### 1.4类

- 启动类ProductServiceApplication

  ```java
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import cn.hutool.core.util.NetUtil;
   
  @SpringBootApplication
  public class ProductServiceApplication {
      public static void main(String[] args) {
          int port = 8080;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(ProductServiceApplication.class).run(args);
      }
  }
  ```

- 实体类Product

  ```java
  import lombok.AllArgsConstructor;
  import lombok.Builder;
  import lombok.Data;
  import lombok.NoArgsConstructor;
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public class Product {
      private int id;
      private String name;
      private int price;
  }
  ```

- 服务类ProductService

  ```java
  import com.tzy.testcloud.pojo.Product;
  import org.springframework.stereotype.Service;
  
  import java.util.ArrayList;
  import java.util.List;
  
  @Service
  public class ProductService {
      public List<Product> listProducts(){
          List<Product> ps = new ArrayList<>();
          ps.add(Product.builder().id(1).name("product a").price(50).build());
          ps.add(Product.builder().id(2).name("product b").price(100).build());
          ps.add(Product.builder().id(3).name("product c").price(150).build());
          return ps;
      }
  }
  ```

- 控制类ProductController

  ```java
  import com.tzy.testcloud.pojo.Product;
  import com.tzy.testcloud.service.ProductService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Controller;
  import org.springframework.ui.Model;
  import org.springframework.web.bind.annotation.RequestMapping;
  
  import java.util.List;
  
  @Controller
  public class ProductController {
    
      @Autowired
      ProductService productService;
       
      @RequestMapping("/products")
      public Object products(Model m) {
          List<Product> ps = productService.listProducts();
          m.addAttribute("ps", ps);
          return "products";
      }
  }
  ```

### 1.5启动测试

- http://127.0.0.1:8080/products

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614113821119-1910459863.png)

## 2 微服务应用问题

### 2.1分布式和集群周边服务

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614130838426-712537396.png)

以上是很简单的分布式结构，围绕这个结构，我们有时候还需要做如下事情：

1. 哪些微服务是如何彼此调用的？ sleuth 服务链路追踪
2. 如何在微服务间共享配置信息？配置服务 Config Server
3. 如何让配置信息在多个微服务之间自动刷新？ RabbitMQ 总线 Bus
4. 如果数据微服务集群都不能使用了， 视图微服务如何去处理? 断路器 Hystrix
5. 视图微服务的断路器什么时候开启了？什么时候关闭了？ 断路器监控 Hystrix Dashboard
6. 如果视图微服务本身是个集群，那么如何进行对他们进行聚合监控？ 断路器聚合监控 Turbine Hystrix Dashboard
7. 如何不暴露微服务名称，并提供服务？ Zuul 网关



## 3 Eureka注册中心项目搭建

### 3.1 项目结构(和上面无关,父子工程)

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614135415865-1188266018.png)

### 3.2 配置文件

- 主---------pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.tzy</groupId>
      <artifactId>testcloud</artifactId>
      <version>1.0-SNAPSHOT</version>
      <modules>
          <module>eureka-server</module>
      </modules>
      <packaging>pom</packaging>
  
      <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.0.3.RELEASE</version>
          <relativePath/>
      </parent>
  
      <properties>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
          <java.version>1.8</java.version>
          <spring-cloud.version>Finchley.RELEASE</spring-cloud.version>
      </properties>
  
      <dependencies>
          <!--lombok-->
          <dependency>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <optional>true</optional>
          </dependency>
          <!--hutool-->
          <dependency>
              <groupId>cn.hutool</groupId>
              <artifactId>hutool-all</artifactId>
              <version>4.3.1</version>
          </dependency>
  
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-test</artifactId>
              <scope>test</scope>
          </dependency>
          <dependency>
              <groupId>cn.hutool</groupId>
              <artifactId>hutool-all</artifactId>
              <version>4.3.1</version>
          </dependency>
      </dependencies>
  
      <dependencyManagement>
          <dependencies>
              <dependency>
                  <groupId>org.springframework.cloud</groupId>
                  <artifactId>spring-cloud-dependencies</artifactId>
                  <version>${spring-cloud.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
          </dependencies>
      </dependencyManagement>
  
  </project>
  ```

- eureka-server---------pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>eureka-server</artifactId>
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
          </dependency>
      </dependencies>
  
  </project>
  ```

- eureka-server---------application.yml

  ```yml
  server:
    port: 8761
  eureka:
    instance:
      hostname: localhost #表示主机名称。
    client:
      registerWithEureka: false   #表示是否注册到服务器。 因为它本身就是服务器，所以就无需把自己注册到服务器了。
      fetchRegistry: false        #表示是否获取服务器的注册信息，和上面同理，这里也设置为 false。
      serviceUrl:                 #自己作为服务器，公布出来的地址。 比如后续某个微服务要把自己注册到 eureka server, 那么就要使用这个地址： http://localhost:8761/eureka/
        defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
   
  spring:
    application:
      name: eureka-server     #表示这个微服务本身的名称是 eureka-server
  
  
  ```

### 3.3 类

- 类eureka-server---------启动类EurekaServerApplication

  ```java
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
   
  import cn.hutool.core.util.NetUtil;
  
  @SpringBootApplication
  @EnableEurekaServer
  public class EurekaServerApplication {
      public static void main(String[] args) {
          int port = 8761;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(EurekaServerApplication.class).run(args);
      }
  }
  ```

### 3.4启动测试

<http://127.0.0.1:8761/>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614140646356-1848029771.png)



## 4 服务提供项目搭建

### 4.1 项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614145351059-600127654.png)

### 4.2 配置文件

- 主---------pom.xml

  ![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614145712948-1690622806.png)

- product-data-service---------pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>product-data-service</artifactId>
  
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
      </dependencies>
  
  </project>
  ```

- product-data-service---------application.yml

  ```yml
  server:
    port: 8001
  eureka:
    client:
      serviceUrl:   #设置注册中心的地址： 与 eureka-server 中的配置 application.yml 遥相呼应
        defaultZone: http://localhost:8761/eureka/
  
  spring:
    application:
      name: product-data-service     #表示这个微服务本身的名称是 product-data-service
  ```

### 4.3 类

- 类product-data-service---------启动类ProductDataServiceApplication

  ```java
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
  import cn.hutool.core.util.NetUtil;
  
  @SpringBootApplication
  @EnableEurekaClient
  public class ProductDataServiceApplication {
      public static void main(String[] args) {
          int port = 8001;
           
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
           
          new SpringApplicationBuilder(ProductDataServiceApplication.class).run(args);
      }
  }
  ```

- 类product-data-service---------实体类Product

  ```java
  import lombok.AllArgsConstructor;
  import lombok.Builder;
  import lombok.Data;
  import lombok.NoArgsConstructor;
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public class Product {
      private int id;
      private String name;
      private int price;
  }
  ```

- 类product-data-service---------服务类ProductService

  ```java
  import com.tzy.data.pojo.Product;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.stereotype.Service;
  
  import java.util.ArrayList;
  import java.util.List;
  
  @Service
  public class ProductService {
      @Value("${server.port}")
      String port;
  
      public List<Product> listProducts() {
          List<Product> ps = new ArrayList<>();
          ps.add(Product.builder().id(1).name("product a " + port + ":").price(50).build());
          ps.add(Product.builder().id(2).name("product b " + port + ":").price(100).build());
          ps.add(Product.builder().id(3).name("product c " + port + ":").price(150).build());
          return ps;
      }
  }
  ```

- 类product-data-service---------控制类ProductController

  ```java
  import java.util.List;
  
  import com.tzy.data.pojo.Product;
  import com.tzy.data.service.ProductService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;
   
  
  @RestController
  public class ProductController {
    
      @Autowired
      ProductService productService;
       
      @RequestMapping("/products")
      public Object products() {
          List<Product> ps = productService.listProducts();
          return ps;
      }
  }
  
  ```

### 4.4启动测试

eureka-server启动

product-data-service:8001启动

product-data-service:8002启动

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614144807564-441036470.png)

<http://127.0.0.1:8761/>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614151011300-401040330.png)

<http://localhost:8001/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154132536-1708736780.png)

<http://localhost:8002/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154151330-544669098.png)



## 5 服务调用项目搭建(Ribbon)

> 接下来访问前面注册好的数据微服务了。 
>
> springcloud 提供了两种方式，一种是 Ribbon，一种是 Feign。
>
> Ribbon 是使用 restTemplate 进行调用，并进行客户端负载均衡。
>
> 什么是客户端负载均衡呢？ 在前面服务注册里，注册了8001和8002两个微服务， Ribbon 会从注册中心获知这个信息，然后由 Ribbon 这个客户端自己决定是调用哪个，这个就叫做客户端负载均衡。
>
> Feign 是什么呢？ Feign 是对 Ribbon的封装，调用起来更简单。

### 5.1 项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154448996-756354307.png)

### 5.2 配置文件

- 主---------pom.xml

  ![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614155527188-1239762887.png)

- product-view-service-ribbon---------pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>product-view-service-ribbon</artifactId>
  
      <dependencies>
          <!--eureka客户端-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
          <!--mvc-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
          <!--thymeleaf-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-thymeleaf</artifactId>
          </dependency>
  
      </dependencies>
  </project>
  
  ```

- product-view-service-ribbon---------pom.xml

  ```yml
  server:
    port: 8010
  eureka:
    client:
      serviceUrl:
        defaultZone: http://localhost:8761/eureka/
  spring:
    application:
      name: product-view-service-ribbon
    thymeleaf:
      cache: false
      prefix: classpath:/templates/
      suffix: .html
      encoding: UTF-8
      mode: HTML5
      servlet:
        content-type: text/html
  
  
  ```

### 5.3页面

- product-view-service-ribbon---------products.html

  ```html
  <!DOCTYPE HTML>
  <html xmlns:th="http://www.thymeleaf.org">
  <head>
      <title>products</title>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      <style>
          table {
              border-collapse: collapse;
              width: 400px;
              margin: 20px auto;
          }
  
          td, th {
              border: 1px solid gray;
          }
      </style>
  </head>
  <body>
  <div class="workingArea">
      <table>
          <thead>
          <tr>
              <th>id</th>
              <th>产品名称</th>
              <th>价格</th>
          </tr>
          </thead>
          <tbody>
          <tr th:each="p: ${ps}">
              <td th:text="${p.id}"></td>
              <td th:text="${p.name}"></td>
              <td th:text="${p.price}"></td>
          </tr>
          </tbody>
      </table>
  </div>
  </body>
  </html>
  
  ```

### 5.4 类

- 类product-view-service-ribbon---------启动类ProductViewServiceRibbonApplication

  ```java
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
  import org.springframework.cloud.client.loadbalancer.LoadBalanced;
  import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
  import org.springframework.context.annotation.Bean;
  import org.springframework.web.client.RestTemplate;
   
  import cn.hutool.core.util.NetUtil;
  
  @SpringBootApplication
  @EnableEurekaClient
  //@EnableDiscoveryClient表示用于发现eureka 注册中心的微服务。
  @EnableDiscoveryClient
  public class ProductViewServiceRibbonApplication {
   
      public static void main(String[] args) {
          int port = 8010;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(ProductViewServiceRibbonApplication.class).run(args);
   
      }
      @Bean
      @LoadBalanced
      RestTemplate restTemplate() {
          return new RestTemplate();
      }
       
  }
  
  ```

- 类product-view-service-ribbon---------实体类Product

  ```java
  import lombok.AllArgsConstructor;
  import lombok.Builder;
  import lombok.Data;
  import lombok.NoArgsConstructor;
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public class Product {
      private int id;
      private String name;
      private int price;
  }
  
  ```

- 类product-view-service-ribbon---------客户端类ProductClientRibbon

  ```java
  import java.util.List;
  
  import com.tzy.testribbon.pojo.Product;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Component;
  import org.springframework.web.client.RestTemplate;
   
  
  @Component
  public class ProductClientRibbon {
   
      @Autowired
      RestTemplate restTemplate;
  
      /**
       *  通过 restTemplate 访问 http://PRODUCT-DATA-SERVICE/products ，
       *  而 product-data-service 既不是域名也不是ip地址，而是 数据服务在 eureka 注册中心的名称。
       */
      public List<Product> listProdcuts() {
          return restTemplate.getForObject("http://PRODUCT-DATA-SERVICE/products",List.class);
      }
   
  }
  
  ```

- 类product-view-service-ribbon---------服务类ProductService

  ```java
  import java.util.List;
  
  import com.tzy.testribbon.client.ProductClientRibbon;
  import com.tzy.testribbon.pojo.Product;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
   
  
  @Service
  public class ProductService {
      @Autowired
      ProductClientRibbon productClientRibbon;
      public List<Product> listProducts(){
          return productClientRibbon.listProdcuts();
      }
  }
  ```

- 类product-view-service-ribbon---------控制类ProductController

  ```java
  import java.util.List;
  
  import com.tzy.testribbon.pojo.Product;
  import com.tzy.testribbon.service.ProductService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Controller;
  import org.springframework.ui.Model;
  import org.springframework.web.bind.annotation.RequestMapping;
   
  
  @Controller
  public class ProductController {
    
      @Autowired
      ProductService productService;
       
      @RequestMapping("/products")
      public Object products(Model m) {
          List<Product> ps = productService.listProducts();
          m.addAttribute("ps", ps);
          return "products";
      }
  }
  ```

### 5.5启动测试

eureka-server启动

product-data-service:8001启动

product-data-service:8002启动

product-view-service-ribbon启动

<http://127.0.0.1:8761/>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154334415-587265049.png)

<http://localhost:8001/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154132536-1708736780.png)

<http://localhost:8002/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154151330-544669098.png)

<http://127.0.0.1:8010/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154230150-836013365.png)
![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154244835-1457682307.png)



- 调用视图

  ![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614130838426-712537396.png)

1. 首先数据微服务(product-data-service)和视图微服务(product-view-service-ribbon)都被 eureka (eureka-server)管理起来了。
2. 数据服务(product-view-service-ribbon)是由两个实例的集群组成的，端口分别是 8001 ， 8002
3. 视图微服务(product-view-service-ribbon)通过 注册中心调用微服务， 然后负载均衡到 8001 或者 8002 端口的应用上。



## 6 提取API服务

### 6.1 项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614162759452-1881650353.png)

### 6.2 配置文件

- 主---------pom.xml

  ![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614162831801-1660304514.png)

- product-api---------pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <packaging>jar</packaging>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>product-api</artifactId>
      
  </project>
  
  ```

### 6.3 类

- 类product-api---------实体类Product

  ```java
  import lombok.AllArgsConstructor;
  import lombok.Builder;
  import lombok.Data;
  import lombok.NoArgsConstructor;
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public class Product {
      private int id;
      private String name;
      private int price;
  }
  
  ```

### 6.4删除其他工程的实体类

### 6.5对于删除了实体类的工程添加pom依赖

```xml
        <dependency>
            <groupId>com.tzy</groupId>
            <artifactId>product-api</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>

```





## 7 服务调用项目搭建(Feign)

> Feign 是什么呢？ Feign 是对 Ribbon的封装，使用注解的方式，调用起来更简单。。。 也是主流的方式~

### 7.1 项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614165417044-1772931481.png)

### 7.2 配置文件

- 主---------pom.xml

  ![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614164616264-106177381.png)

- product-service-view-feign---------pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>product-service-view-feign</artifactId>
  
      <dependencies>
          <!--api-->
          <dependency>
              <groupId>com.tzy</groupId>
              <artifactId>product-api</artifactId>
              <version>1.0-SNAPSHOT</version>
              <scope>compile</scope>
          </dependency>
          <!--eureka-client-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
          <!--feign-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-openfeign</artifactId>
          </dependency>
          <!--mvc-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
          <!--thymeleaf-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-thymeleaf</artifactId>
          </dependency>
      </dependencies>
  
  </project>
  
  ```

- product-service-view-feign---------application.yml

  ```yml
  server:
    port: 8020
  eureka:
    client:
      serviceUrl:
        defaultZone: http://localhost:8761/eureka/
  spring:
    application:
      name: product-service-view-feign
    thymeleaf:
      cache: false
      prefix: classpath:/templates/
      suffix: .html
      encoding: UTF-8
      mode: HTML5
      servlet:
        content-type: text/html
  
  
  ```



### 7.3页面

- 类product-service-view-feign---------products.html

  ```html
  <!DOCTYPE HTML>
  <html xmlns:th="http://www.thymeleaf.org">
  <head>
      <title>products</title>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
      <style>
          table {
              border-collapse: collapse;
              width: 400px;
              margin: 20px auto;
          }
  
          td, th {
              border: 1px solid gray;
          }
      </style>
  </head>
  <body>
  <div class="workingArea">
      <table>
          <thead>
          <tr>
              <th>id</th>
              <th>产品名称</th>
              <th>价格</th>
          </tr>
          </thead>
          <tbody>
          <tr th:each="p: ${ps}">
              <td th:text="${p.id}"></td>
              <td th:text="${p.name}"></td>
              <td th:text="${p.price}"></td>
          </tr>
          </tbody>
      </table>
  </div>
  </body>
  </html>
  ```

  

### 7.4 类

- 类product-service-view-feign---------启动类ProductViewServiceFeignApplication

  ```java
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
  import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
  import org.springframework.cloud.openfeign.EnableFeignClients;
  import cn.hutool.core.util.NetUtil;
  
  @SpringBootApplication
  @EnableEurekaClient
  //@EnableDiscoveryClient表示用于发现eureka 注册中心的微服务。
  @EnableDiscoveryClient
  //@EnableFeignClients表示自己为Feign客户端
  @EnableFeignClients
  public class ProductViewServiceFeignApplication {
   
      public static void main(String[] args) {
          int port = 8020;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(ProductViewServiceFeignApplication.class).properties("server.port=" + port).run(args);
   
      }
  }
  ```

- 类product-service-view-feign---------客户端类ProductClientFeign

  ```java
  import com.tzy.data.pojo.Product;
  import org.springframework.cloud.openfeign.FeignClient;
  import org.springframework.stereotype.Component;
  import org.springframework.web.bind.annotation.GetMapping;
  
  import java.util.List;
  
  @Component
  @FeignClient(value = "PRODUCT-DATA-SERVICE")
  public interface ProductClientFeign {
   
      @GetMapping("/products")
      public List<Product> listProdcuts();
  }
  
  ```

- 类product-service-view-feign---------服务类ProductService

  ```java
  import com.tzy.data.pojo.Product;
  import com.tzy.testfeign.client.ProductClientFeign;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
   
  
  @Service
  public class ProductService {
      @Autowired
      ProductClientFeign productClientFeign;
      public List<Product> listProducts(){
          return productClientFeign.listProdcuts();
   
      }
  }
  
  ```

- 类product-service-view-feign---------控制类ProductController

  ```java
  import java.util.List;
  
  import com.tzy.data.pojo.Product;
  import com.tzy.testfeign.service.ProductService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Controller;
  import org.springframework.ui.Model;
  import org.springframework.web.bind.annotation.RequestMapping;
   
  
  @Controller
  public class ProductController {
    
      @Autowired
      ProductService productService;
       
      @RequestMapping("/products")
      public Object products(Model m) {
          List<Product> ps = productService.listProducts();
          m.addAttribute("ps", ps);
          return "products";
      }
  }
  
  ```



### 7.5启动测试

eureka-server启动

product-data-service:8001启动

product-data-service:8002启动

product-view-service-ribbon启动

<http://127.0.0.1:8761/>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614173458380-447899942.png)

<http://localhost:8001/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154132536-1708736780.png)

<http://localhost:8002/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614154151330-544669098.png)

<http://127.0.0.1:8020/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614173536672-1537443594.png)
![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614173602134-1253065862.png)



## 8 服务链路

> 在前面的例子里，我们有两个微服务，分别是数据服务和视图服务，随着业务的增加，就会有越来越多的微服务存在，他们之间也会有更加复杂的调用关系。
> 这个调用关系，仅仅通过观察代码，会越来越难以识别，所以就需要通过 zipkin 服务链路追踪服务器 这个东西来用图片进行识别了。

### 8.1 下载zipkin-server-2.10.1-exec.jar

<https://download.csdn.net/download/tzy70416450/11241753>

### 8.2添加需要监控的微服务依赖和配置

- eureka-server（不需要加）
- product-api(不需要加)
- product-view-service-ribbon(不用了)
- product-data-service
- product-service-view-feign

> 1-----pom.xml添加依赖

```xml
<!--zipkin-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

> 2-----application.yml添加配置

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614175625193-1416878423.png)

```yml
spring:
  zipkin:
    base-url: http://localhost:9411
```

> 3-----添加持续抽样
>
> 在启动类里配置 Sampler 抽样策略： ALWAYS_SAMPLE 表示持续抽样

```java
@Bean
public Sampler defaultSampler() {
	return Sampler.ALWAYS_SAMPLE;
}  
```

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614175910162-698678450.png)

### 8.3启动测试

启动zipkin

```java
java -jar zipkin-server-2.10.1-exec.jar
```

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614180326682-999038991.png)

eureka-server启动

product-data-service:8001启动

product-data-service:8002启动

product-view-service-ribbon启动

- 调用2次接口<http://127.0.0.1:8020/products>
- 监控网<http://localhost:9411/zipkin/dependency>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614181858876-829802505.png)

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190614181933304-672944818.png)



## 9 配置服务器(Service)

### 9.1首先准备git

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617141854136-1440435650.png)



### 9.2 项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617110857045-459805323.png)

### 9.3 配置文件

- 主---------pom.xml

  ![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617111017571-1555655271.png)

- config-server---------pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>config-server</artifactId>
  
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-config-server</artifactId>
          </dependency>
  
      </dependencies>
  </project>
  ```

- config-server---------application.yml

  ```yml
  server:
    port: 8030
  eureka:
    client:
      serviceUrl:   #设置注册中心的地址： 与 eureka-server 中的配置 application.yml 遥相呼应
        defaultZone: http://localhost:8761/eureka/
  
  spring:
    application:
      name: config-server     #表示这个微服务本身的名称是 product-data-service
    cloud:
      config:
        label: master         #表示git的master分支
        server:
          git:
            uri: https://github.com/70416450/cloud-config
            searchPaths: conf       #目录结构
  ```

  



### 9.4 类

- 类config-server---------启动类ConfigServerApplication

  ```java
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
  import org.springframework.cloud.config.server.EnableConfigServer;
  import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
   
  import cn.hutool.core.util.NetUtil;
   
  @SpringBootApplication
  @EnableEurekaClient
  //@EnableConfigServer 配置服务。
  @EnableConfigServer
  //@EnableDiscoveryClient表示用于发现eureka 注册中心的微服务。
  @EnableDiscoveryClient
  public class ConfigServerApplication {
      public static void main(String[] args) {
          int port = 8030;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(ConfigServerApplication.class).run(args);
       
      }
  }
  
  ```

### 9.5启动测试

eureka-server启动

config-server启动

<http://localhost:8030/product-service-view-feign.yml>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617142013224-462026815.png)

<http://localhost:8030/product-service-view-feign-dev.yml>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617142049083-1330356807.png)

<http://localhost:8030/product-service-view-feign-test.yml>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617142128750-2083151042.png)



## 10 配置客户端(Client)

### 10.1改造product-service-view-feign

- pom.xml增加

  ```xml
          <!--config-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-config</artifactId>
          </dependency>
  ```

- 增加bootstrap.yml

  > bootstrap.yml 优先级别比application.yml高，所以有这个文件的情况下，优先访问这个文件
  > bootstrap.yml)

  ```yml
  spring:
    cloud:
      config:
        label: master
        profile: dev    #本次访问的配置项dev or test
        discovery:
          enabled: true
          serviceId: config-server
  ```

- 修改application.yml

  ```yml
  spring:
    application:
      name: product-service-view-feign
  
  ```

- 修改ProductController

  ```java
  import java.util.List;
  
  import com.tzy.data.pojo.Product;
  import com.tzy.testfeign.service.ProductService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.beans.factory.annotation.Value;
  import org.springframework.cloud.context.config.annotation.RefreshScope;
  import org.springframework.stereotype.Controller;
  import org.springframework.ui.Model;
  import org.springframework.web.bind.annotation.RequestMapping;
   
  
  @Controller
  @RefreshScope
  public class ProductController {
    
      @Autowired
      ProductService productService;
  
      @Value("${version}")
      String version;
  
      @RequestMapping("/products")
      public Object products(Model m) {
          List<Product> ps = productService.listProducts();
          m.addAttribute("version", version);
          m.addAttribute("ps", ps);
          return "products";
      }
  }
  ```

- 修改products.html增加版本显示

  ```html
          <tr>
              <td align="center" colspan="3">
                  <p th:text="${version}" >springcloud version unknown</p>
              </td>
          </tr>
  
  ```

### 10.2启动测试

启动 EurekaServerApplication

启动ConfigServerApplication

启动ProductDataServiceApplication

启动ProductViewServiceFeignApplication
<http://localhost:8020/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617143500177-1780027252.png)

到此已经可以读取服务器端配置信息，但是修改git上的配置文件，必须重启服务才可以看到信息改变。

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617144054260-1789437203.png)



## 11 消息总线BUS

springCloud 通过 rabbitMQ 来进行消息广播，以达到有配置信息发生改变的时候，广播给多个微服务的效果。
所以需要先安装 rabbitMQ 服务器。



### 11.1改造product-service-view-feign

- pom.xml

  ```xml
          <!--执行器-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-actuator</artifactId>
          </dependency>
          <!--mq-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-bus-amqp</artifactId>
          </dependency>
  ```

- bootstrap.yml(增加bus和mq配置)

  ```yml
  spring:
    cloud:
      config:
        label: master
        profile: dev    #本次访问的配置项
        discovery:
          enabled: true
          serviceId: config-server
      bus:
        enabled: true
        trace:
          enabled: true
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest 
  ```

- application.yml路径访问允许

  ```yml
  spring:
    application:
      name: product-service-view-feign
  management:
    endpoints:
      web:
        exposure:
          include: "*"
        cors:
          allowed-origins: "*"
          allowed-methods: "*"
  
  ```

- 启动类ProductServiceViewFeignApplication增加mq端口检测

  ```java
  import brave.sampler.Sampler;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
  import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
  import org.springframework.cloud.openfeign.EnableFeignClients;
  import cn.hutool.core.util.NetUtil;
  import org.springframework.context.annotation.Bean;
  
  @SpringBootApplication
  @EnableEurekaClient
  //@EnableDiscoveryClient表示用于发现eureka 注册中心的微服务。
  @EnableDiscoveryClient
  //@EnableFeignClients表示自己为Feign客户端
  @EnableFeignClients
  public class ProductServiceViewFeignApplication {
   
      public static void main(String[] args) {
          //判断 rabiitMQ 是否启动
          int rabbitMQPort = 5672;
          if(NetUtil.isUsableLocalPort(rabbitMQPort)) {
              System.err.printf("未在端口%d 发现 rabbitMQ服务，请检查rabbitMQ 是否启动", rabbitMQPort );
              System.exit(1);
          }
          
          int port = 8020;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(ProductServiceViewFeignApplication.class).run(args);
   
      }
      @Bean
      public Sampler defaultSampler() {
          return Sampler.ALWAYS_SAMPLE;
      }
  }
  
  ```

- 新增FreshConfigUtil  用于访问路径：/actuator/bus-refresh

  ```java
  import java.util.HashMap;
   
  import cn.hutool.http.HttpUtil;
   
  public class FreshConfigUtil {
   
      public static void main(String[] args) {
          HashMap<String,String> headers =new HashMap<>();
          headers.put("Content-Type", "application/json; charset=utf-8");
          System.out.println("因为要去git获取，还要刷新config-server, 会比较卡，所以一般会要好几秒才能完成，请耐心等待");
   
          String result = HttpUtil.createPost("http://localhost:8020/actuator/bus-refresh").addHeaders(headers).execute().body();
          System.out.println("result:"+result);
          System.out.println("refresh 完成");
      }
  }
  
  ```

  

### 11.2对服务链路追踪的影响

因为视图服务进行了改造，支持了 rabbitMQ, 那么在默认情况下，它的信息就不会进入 Zipkin了。 在Zipkin 里看不到视图服务的资料了。

为了解决这个问题，在启动 Zipkin 的时候 带一个参数就好了：

```
java -jar zipkin-server-2.10.1-exec.jar --zipkin.collector.rabbitmq.addresses=localhost

```



### 11.3启动测试

启动 EurekaServerApplication

启动ConfigServerApplication

启动ProductDataServiceApplication

启动ProductViewServiceFeignApplication
<http://localhost:8020/products>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617180749938-402932805.png)

运行FreshConfigUtil ，再次访问

http://localhost:8020/products

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617181256068-1601836187.png)





## 12 断路器 HYSTRIX

> 所谓的断路器，就是当被访问的微服务无法使用的时候，当前服务能够感知这个现象，并且提供一个备用的方案出来。

### 12.1改造product-view-service-feign

- pom.xml

  ```xml
          <!--hystrix断路器-->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
          </dependency>
  
  ```

- 新建Feign客户端接口实现类ProductClientFeignHystrix

  ```java
  import java.util.ArrayList;
  import java.util.List;
  
  import com.tzy.data.pojo.Product;
  import org.springframework.stereotype.Component;
  
  @Component
  public class ProductClientFeignHystrix implements ProductClientFeign{
      public List<Product> listProdcuts(){
          List<Product> result = new ArrayList<>();
          result.add(new Product(0,"产品数据微服务不可用",0));
          return result;
      }
  }
  
  ```

- application.yml增加Hystrix配置

  ```yml
  spring:
    application:
      name: product-service-view-feign
  feign.hystrix.enabled: true
  management:
    endpoints:
      web:
        exposure:
          include: "*"
        cors:
          allowed-origins: "*"
          allowed-methods: "*"
  
  ```

- 修改Feign客户端接口ProductClientFeign

  ```java
  import com.tzy.data.pojo.Product;
  import org.springframework.cloud.openfeign.FeignClient;
  import org.springframework.stereotype.Component;
  import org.springframework.web.bind.annotation.GetMapping;
  
  import java.util.List;
  
  @Component
  //@FeignClient(value = "PRODUCT-DATA-SERVICE")
  @FeignClient(value = "PRODUCT-DATA-SERVICE",fallback = ProductClientFeignHystrix.class)
  public interface ProductClientFeign {
   
      @GetMapping("/products")
      public List<Product> listProdcuts();
  }
  
  ```

### 12.2启动测试

启动 EurekaServerApplication

启动ConfigServerApplication

启动ProductDataServiceApplication

启动ProductViewServiceFeignApplication

http://localhost:8020/products

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617181256068-1601836187.png)

断开ProductDataServiceApplication

http://localhost:8020/products

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617183721691-1001845422.png)



## 13 断路器监控Dashboard(豪猪)

### 13.1改造product-view-service-feign启动类

增加``` @EnableCircuitBreaker```以使得它可以把信息共享给监控中心。

### 13.2 项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617184605264-836558610.png)



### 13.3 配置文件

- pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>hystrix-dashboard</artifactId>
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-actuator</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
          </dependency>
  
      </dependencies>
  
  </project>
  
  ```

- application.yml

  ```yml
  server:
    port: 8040
  spring:
    application:
      name: hystrix-dashboard
  
  ```

### 13.4 类

- 启动类ProductServiceHystrixDashboardApplication

  ```java
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
   
  import cn.hutool.core.util.NetUtil;
   
  @SpringBootApplication
  //@EnableHystrixDashboard监控
  @EnableHystrixDashboard
  public class ProductServiceHystrixDashboardApplication {
      public static void main(String[] args) {
          int port = 8040;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(ProductServiceHystrixDashboardApplication.class).properties("server.port=" + port).run(args);
      }
  }
  
  ```

- AccessViewService用于不停的访问服务，这样可以看到监控效果

  ```java
  import cn.hutool.core.thread.ThreadUtil;
  import cn.hutool.http.HttpUtil;
  
  /**
   * 用于不停的访问服务，这样可以看到监控效果
   */
  public class AccessViewService {
      public static void main(String[] args) {
          while(true) {
              ThreadUtil.sleep(1000);
              try {
                  String html= HttpUtil.get("http://127.0.0.1:8020/products");
                  System.out.println("html length:" + html.length());
              }
              catch(Exception e) {
                  System.err.println(e.getMessage());
              }
          }
      }
  }
  
  ```

### 13.5启动测试

启动 EurekaServerApplication 

启动ConfigServerApplication

启动ProductDataServiceApplication

启动ProductServiceViewFeignApplication

启动ProductServiceHystrixDashboardApplication

运行AccessViewService 来周期性地访问 http://127.0.0.1:8020/products。 因为只有访问了，监控里才能看到数据。

打开监控地址<http://localhost:8040/hystrix>

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617185449487-766375168.png)

在最上面输入http://localhost:8020/actuator/hystrix.stream

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617185733969-940941165.png)

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617185831730-753037338.png)

此时关闭数据服务ProductDataServiceApplication，再观察，不一会儿红色的数据就达到 100%啦

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190617190046051-327093927.png)





## 14 断路器聚合监控

> 上述13是断路器监控，但是微服务通常会是多个实例组成的一个集群。 倘若集群里的实例比较多，难道要挨个挨个去监控这些实例吗？ 何况有时候，根据集群的需要，会动态增加或者减少实例，监控起来就更麻烦了。
> 所以为了方便监控集群里的多个实例，springCloud 提供了一个 turbine 项目，它的作用是把一个集群里的多个实例汇聚在一个 turbine里，这个然后再在 断路器监控里查看这个 turbine, 这样就能够在集群层面进行监控啦。

### 14.1改造hystrix-dashboard访问类

```java
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;

/**
 * 用于不停的访问服务，这样可以看到监控效果
 */
public class AccessViewService {

    public static void main(String[] args) {

        while(true) {
            ThreadUtil.sleep(1000);
            access(8020);
            access(8021);
        }

    }

    public static void access(int port) {
        try {
            String html= HttpUtil.get(String.format("http://127.0.0.1:%d/products",port));
            System.out.printf("%d 地址的视图服务访问成功，返回大小是 %d%n" ,port, html.length());
        }
        catch(Exception e) {
            System.err.printf("%d 地址的视图服务无法访问%n",port);
        }
    }
}

```



### 14.2 项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190618095744938-956699356.png)



### 14.3 配置文件

- pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>turbine</artifactId>
  
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-actuator</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-turbine</artifactId>
          </dependency>
  
      </dependencies>
  </project>
  
  ```

- application.yml

  ```yml
  server:
    port: 8050
  spring:
    application.name: turbine
  turbine:
    aggregator:
      clusterConfig: default
    appConfig: product-view-service-feign  # 配置Eureka中的serviceId列表，表明监控哪些服务
    clusterNameExpression: new String("default")
  eureka:
    client:
      serviceUrl:
        defaultZone: http://localhost:8761/eureka/
  
  
  ```

### 14.4类

- 启动类ProductServiceTurbineApplication

  ```java
  import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.netflix.turbine.EnableTurbine;
   
  import cn.hutool.core.util.NetUtil;
   
  @SpringBootApplication
  @EnableTurbine
  @EnableAutoConfiguration(exclude = {
  org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
  })
  public class ProductServiceTurbineApplication {
      public static void main(String[] args) {
          int port = 8050;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(ProductServiceTurbineApplication.class).run(args);
      }
  }
  
  ```

### 14.5启动测试

启动 EurekaServerApplication 

启动ConfigServerApplication

启动ProductDataServiceApplication

启动ProductServiceViewFeignApplication:8020

启动ProductServiceViewFeignApplication:8021

启动ProductServiceHystrixDashboardApplication

启动ProductServiceTurbineApplication

运行AccessViewService 来周期性地访问 http://127.0.0.1:8020/products,

http://127.0.0.1:8021/products 因为只有访问了，监控里才能看到数据。

打开监控地址<http://localhost:8040/hystrix>

输入http://localhost:8020/actuator/hystrix.stream

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190618103541474-1230500363.png)

输入http://localhost:8021/actuator/hystrix.stream

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190618103513453-2061297395.png)

输入http://localhost:8050/turbine.stream

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190618103550456-503250999.png)



## 15 网关 ZUUL

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190618111942890-827889842.png)

### 15.1 项目结构

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190618113833477-2098303553.png)



### 15.2 配置文件

- pom.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <parent>
          <artifactId>testcloud</artifactId>
          <groupId>com.tzy</groupId>
          <version>1.0-SNAPSHOT</version>
      </parent>
      <modelVersion>4.0.0</modelVersion>
  
      <artifactId>zuul</artifactId>
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
          </dependency>
      </dependencies>
  
  </project>
  
  ```

- application.yml

  ```yml
  server:
    port: 8060
  zuul:
    routes:
      api-a:
        path: /api-data/**
        serviceId: PRODUCT-DATA-SERVICE
      api-b:
        path: /api-view/**
        serviceId: PRODUCT-VIEW-SERVICE-FEIGN
  
  ```

### 15.3 类

- 启动类ProductServiceZuulApplication

  ```java
  import cn.hutool.core.util.NetUtil;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  import org.springframework.boot.builder.SpringApplicationBuilder;
  import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
  import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
  import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
  
  @SpringBootApplication
  @EnableZuulProxy
  @EnableEurekaClient
  @EnableDiscoveryClient
  public class ProductServiceZuulApplication {
      public static void main(String[] args) {
          int port = 8060;
          if(!NetUtil.isUsableLocalPort(port)) {
              System.err.printf("端口%d被占用了，无法启动%n", port );
              System.exit(1);
          }
          new SpringApplicationBuilder(ProductServiceZuulApplication.class).run(args);
   
      }
  }
  
  ```

### 15.4 启动测试

启动 EurekaServerApplication

启动ConfigServerApplication

启动ProductDataServiceApplication

启动ProductViewServiceFeignApplication

启动 ProductServiceZuulApplication

http://localhost:8060/api-data/products

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190618114602157-1157172525.png)

http://localhost:8060/api-view/products

![](https://img2018.cnblogs.com/blog/1235870/201906/1235870-20190618114740560-795248213.png)

这样就可以访问数据微服务和视微服务集群了，并且无需去记住那么多ip地址和端口号了。 







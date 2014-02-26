#Spring RSF RPC Framework


Spring RSF 是基于 RESTEasy & Spring 构建的 RESTful 风格、轻量级 RPC 框架。


### Introduction

采用 Spring AOP + RESTEasy 实现。基于 HTTP RESTFul 风格，JSON & XML 数据协议格式转输。对于调用者无需了解具体协议及URL参数，完全基于远程对象接口方式进行数据交互。

客户端对多 Server 提供负载均衡机制，可轻松实现自定义的路由策略(目前支持随机和轮询)。当集群远程服务器不可用时支持重试机制，动态监测和管理 Server 列表。能实时从远程获取及更新 Server 列表。



## Quick Start


##### 服务提供者

__1. 加入工程依赖__

```xml
<dependency>
	<groupId>org.springcn.rsf</groupId>
	<artifactId>spring-rsf</artifactId>
	<packaging>jar</packaging>
	<version>1.0.1</version>
<dependency>
```
*在 Server 端和 Clinet 的工程都需添加该依赖。*


__2.添加至 Server 端的 web.xml__

```xml
<context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>WEB-INF/classes/spring/*.xml</param-value>
</context-param>
<listener>
	<listener-class>
		org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap
	</listener-class>
</listener>

<listener>
	<listener-class>
		org.jboss.resteasy.plugins.spring.SpringContextLoaderListener
	</listener-class>
</listener>

<servlet>
	<servlet-name>resteasy-servlet</servlet-name>
	<servlet-class>
		org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher
	</servlet-class>
</servlet>

<!-- 指定是 API 的根路径 -->
<servlet-mapping>
	<servlet-name>resteasy-servlet</servlet-name>
	<url-pattern>/*</url-pattern>
</servlet-mapping>
```

__3.编写业务服务接口__


```java

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProductService {
 
    /**
     * 创建产品对象 
     * @param productDto
     * @return
     */
    @POST
    ProductDto createProduct(ProductDto productDto) ;

    /**
     * 查询所有产品
     * @return
     */
    @GET
    @Path("list")
    List<ProductDto> listProducts();
}
```

*该接口一般以 jar 包形式发布给调用者。*

##### 调用者

__1. 添加依赖__

引入服务接口(e.g: product-api.jar )及 spring-rsf 的包依赖。


__2. 将服务接口注入__

将如下配置添加至 Spring 配置文件中，以 Bean 方式注入。
 
```xml
<bean id="productService" class="org.springcn.rsf.proxy.RSFClientFactoryBean">
	<!-- 指定服务接口 -->
	<property name="serviceInterface" value="org.springcn.rsf.example.rpc.ProductService" />
    <property name="connectionTimeout" value="5000" />
    <property name="readTimeout" value="5000" />
    <property name="maxTotalConnections" value="30" />
    <property name="defaultMaxConnectionsPerHost" value="20" />
    
    <!-- 重试次数 -->
    <property name="retry" value="1" />
    
    <!-- 服务列表(ip:port/context-root) -->
    <property name="serverList">
    	<list>
        	<value>127.0.0.1:8080</value>
        </list>
	</property>
</bean>
```

__3.调用产品服务__

```

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-rpc-test.xml")
public class ProductServiceTest {
    
    @Autowired
    ProductService productService;
    
    @Test
    public void listProducts() {
        List<ProductDto> products = productService.listProducts();
        AssertTrue(products.size() > 0)
    }
}
```







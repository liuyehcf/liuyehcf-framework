   * [简介](#简介)
   * [如何使用](#如何使用)

# 简介

`rpc-ares`框架用于简化http-client端的代码

一般地，如果要进行http调用，我们需要进行如下几部操作

1. 使用一个http框架，例如apache的httpclient
1. 创建http request
    * 设置http-server的host、port
    * 设置api-path
    * 设置请求参数（包括query param、request body等等）
1. 执行http调用
1. 获取http response
    * 将response中的数据反序列化成对应的JavaBean，或者进行一些异常处理
    
而这些步骤中，大部分都是一些通用的代码，而`rpc-ares`就对这些通用代码进行了封装，我们可以简单通过几个注解就能完成配置，而无需编写这些重复而又琐碎的代码

# 如何使用

__引入maven依赖__

```xml
<dependency>
    <groupId>com.github.liuyehcf</groupId>
    <artifactId>rpc-ares</artifactId>
    <version>1.0.7</version>
</dependency>
```

`rpc-ares`集成了`spring-boot-starter`，在`spring-boot`的应用中使用它非常便捷

__第一步：定义接口__

比如有个系统提供了查询用户信息的http-api，请求示例如下

```
curl http://192.168.0.1:8080/user/get?id=12345

{
    "id":12345,
    "firstName":"三",
    "lastName":"张",
    "age":20
}
```

我们可以在我们的系统中定义如下接口`UserService`

* 在方法上使用注解`AresMethod`，指明`path`
* 在方法入参上使用注解`AresRequestParam`，指明入参名字

```Java
package com.github.liuyehcf.framework.rpc.ares.readme;

import com.github.liuyehcf.framework.rpc.ares.AresMethod;
import com.github.liuyehcf.framework.rpc.ares.AresRequestParam;

public interface UserService {

    @AresMethod(path = "/user/get")
    UserInfo getUser(@AresRequestParam(name = "id") int id);

    class UserInfo {
        private Integer id;
        private String firstName;
        private String lastName;
        private Integer age;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "id=" + id +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
```

__第二步：创建代理类的实例__

在`Spring`的环境中，我们仅需要通过一个注解便可完成这一步操作，在Spring启动之后，会`rpc-ares`框架会自动扫描该注解，并通过动态代理生成`UserService`的实现类。该实现类就封装了http调用的通用逻辑

* 定义一个字段，其类型为第一步定义的接口
* 在字段上标记注解`AresConsumer`，并指明提供该api的server的`host`以及`port`。该参数的配置支持Spring占位符，例如`host = "${xxx.yyy}"`
    * 这里我填的是`127.0.0.1`和`8080`，因为我在本地的8080端口启动了一个`http-server`，提供了`user/get`的api

```Java
package com.github.liuyehcf.framework.rpc.ares.readme;

import com.github.liuyehcf.framework.rpc.ares.AresConsumer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @AresConsumer(host = "127.0.0.1", port = "8080")
    private UserService userService;
}
```

__第三步：在其他业务Bean中使用该接口进行http的调用__

```Java
package com.github.liuyehcf.framework.rpc.ares.readme;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class BizComponent {

    @Resource
    private UserService userService;

    @PostConstruct
    public void business() {
        UserService.UserInfo user = userService.getUser(1);
        System.out.println(user);
    }
}
```

   * [Introduction](#introduction)
   * [How to Use](#how-to-use)

# Introduction

The `rpc-ares` framework is used to simplify the code for the HTTP client.

Typically, when making an HTTP call, we need to perform the following steps:

1. Use an HTTP framework, such as Apache's HttpClient.
2. Create an HTTP request.
    * Set the host and port of the HTTP server.
    * Set the API path.
    * Set request parameters (including query parameters, request body, etc.).
3. Execute the HTTP call.
4. Get the HTTP response.
    * Deserialize the data from the response into the corresponding JavaBean or perform some exception handling.

Most of these steps involve common code, and `rpc-ares` encapsulates these common codes. We can easily configure them using a few annotations without having to write repetitive and tedious code.

# How to Use

__Add Maven Dependency__

```xml
<dependency>
    <groupId>com.github.liuyehcf</groupId>
    <artifactId>rpc-ares</artifactId>
    <version>1.0.7</version>
</dependency>
```

`rpc-ares` integrates with `spring-boot-starter`, making it very convenient to use in a Spring Boot application.

__Step 1: Define an Interface__

For example, if a system provides an HTTP API to query user information, the request may look like this:

```
curl http://192.168.0.1:8080/user/get?id=12345

{
    "id":12345,
    "firstName":"三",
    "lastName":"张",
    "age":20
}
```

We can define the following interface `UserService` in our system:

* Use the `AresMethod` annotation on the method to specify the `path`.
* Use the `AresRequestParam` annotation on the method parameters to specify the parameter names.

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

__Step 2: Create an Instance of the Proxy Class__

In a Spring environment, we only need to use an annotation to complete this step. After Spring starts up, the `rpc-ares` framework will automatically scan for this annotation and generate an implementation class of `UserService` through dynamic proxy. This implementation class encapsulates the common logic for HTTP calls.

* Define a field with the type of the interface defined in step 1.
* Annotate the field with `AresConsumer` and specify the `host` and `port` of the server providing this API. The configuration for these parameters supports Spring placeholders, for example, `host = "${xxx.yyy}"`.
    * Here, I have filled in `127.0.0.1` and `8080` because I have started an `http-server` on my local machine on port 8080, which provides the `user/get` API.

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

__Step 3: Use the Interface in Other Business Beans for HTTP calls__

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
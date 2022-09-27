### 项目介绍：cs-mall

一个微服务项目。

### 项目组织架构：

mall-admin: 后台管理

mall-auth: Oauth2授权服务

mall-gateway: 网关服务，且作为鉴权中心进行对权限的鉴定，并将其分发给下游的微服务

mall-common: 公共服务，包括一些公共组件

mall-search:  整合elasticsearch8.2.0

mall-mbg: 根据数据库自动生成的代码模块



### 项目技术栈：

```
springboot：2.7.0/2.6.4
springcloud：2021.0.1
springcloud alibaba：2021.0.1
spring-cloud-starter-oauth2：2.2.5.RELEASE
mybatis-generator：1.4.1
mybatis：3.5.9
Elasticsearch：8.2.0
rabbitmq：3.9.11
Redis：5.0.14
seata:1.4.2
```




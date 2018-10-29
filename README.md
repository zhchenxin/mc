# 消息中心

## 安装

1、安装依赖：

- mysql
- redis

mysql 的初始化文件为：src/main/resources/init.sql

2、下载jar文件

在 release 中下载jar文件。


3、创建配置文件`application.properties`，配置文件与jar文件放在同级目录下

文件配置如下

```
server.port=8080

spring.datasource.url=jdbc:mysql://106.12.7.187:3306/mc?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=fsQFmANTgHjilVFe

# REDIS
redis.host=106.12.7.187

# 每一个实例的消费者个数
customer_count=10
```

4、启动jar

在jar目录下，运行命令：`nohup java -jar mc-1.0.jar &`

5、测试

打开浏览器，访问网站。

## 功能

1. 基于发布订阅模式
2. 支持延迟消息
3. 支持消息日志功能
4. 失败消息重试手动机制
5. 定时任务功能
6. 定时任务暂停和重启
7. 支持分布式部署

下一版本功能：

1. 消费者并发数限制
2. 消费者暂停和重启
3. 消息日志分表

## 消息发布接口

api：`topic/{name}/push`

name参数为topic的名称

参数：

```
{
    "message": "",
    "delay": 0
}
```

message 参数为消费者内容，delay参数为消息延迟时间，单位秒
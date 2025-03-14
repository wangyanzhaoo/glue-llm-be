## glue-llm-be

烟草大模型 

本项目基于 Spring Boot3.2.x、Spring Boot Jpa、Spring Security 开发

## 准备环境

- JDK：AdoptOpenJdk21
- IDE：IDEA 2023.3
- 数据库：MySQL8，Redis7
- 容器：Docker，WSL2

## Commits 命名规范

```
💥 feat(模块): 添加了个很棒的功能
🐛 fix(模块): 修复了一些 bug
📝 docs(模块): 更新了一下文档
🏰 chore(模块): 对脚手架做了些更改
🔧 congig(模块): 对配置进行了改动
```



# 多数据源部分

## 主数据源部分

主数据源数据加上@PrimaryEntity修饰，repo用@PrimaryRepositoryMarker修饰，查询用JPAQueryFactoryPrimary，事务默认的@Transactional或@Transactional(value = "primaryTransactionManager")

## 次数据源部分

次数据源数据加上@SecondEntity修饰，repo用@SecondaryRepositoryMarker修饰，查询用JPAQueryFactorySecond，事务用@Transactional(value = "secondaryTransactionManager")

## 同时更新数据

同时更新数据事务用@Transactional(value = "transactionManager")

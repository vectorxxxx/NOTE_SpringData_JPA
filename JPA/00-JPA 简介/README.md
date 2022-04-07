> 笔记来源：[尚硅谷jpa开发教程全套完整版(初学者零基础入门)](https://www.bilibili.com/video/BV1vW411M7zp)

[TOC]

# JPA 简介

## 1、JDBC

`JDBC`（Java DataBase Connectivity，Java 语言连接数据库），提供了一组规范即接口，由各个数据库厂商提供这些接口的对应实现，而 Java 应用程序只需要通过 `JDBC` 就可以完成对不同数据库（如 MySQL、Oracle、SQLServer 和 DB2 等）的统一调用

![image-20220406211932074](https://s2.loli.net/2022/04/06/uZiKvOFg8MIBjWJ.png)

## 2、JPA

`JPA`（Java Persistence API，Java 持久化 API），用于对象持久化的 API 

JavaEE5.0 平台标准的 ORM（Object Relational Mapping，对象关系映射）规范，使得应用程序以统一的方式访问持久层
![image-20220406212604290](https://s2.loli.net/2022/04/06/eBCMhW9vIbf32id.png)



## 3、JPA 和 Hibernate 的关系

`JPA` 是 `Hibernate` 的一个抽象（就像 JDBC 和 JDBC 驱动的关系）

- <mark>JPA 是规范</mark>：`JPA` 本质上就是一种 ORM 规范，不是 ORM 框架
  - 因为 `JPA` 并未提供 ORM 实现，它只是制订了一些规范，提供了一些编程的 API 接口，但具体实现则由 ORM 厂商提供实现
- <mark>Hibernate 是实现</mark>：`Hibernate` 除了作为 ORM 框架之外，它也是一种 `JPA` 实现
- 从功能上来说，<mark>JPA 是 Hibernate 功能的一个子集</mark>



## 4、JPA 的供应商

JPA 的目标之一是制定一个可以由很多供应商实现的 API，目前 `Hibernate 3.2+`、`TopLink 10.1+` 以及 `OpenJPA` 都提供了 JPA 的实现

**Hibernate**

- JPA 的“始作俑者”就是 Hibernate 的作者
- Hibernate 从 3.2 开始兼容 JPA

**OpenJPA**

- OpenJPA 是 Apache 组织提供的开源项目

**TopLink**

- TopLink 以前需要收费，如今开源了



## 5、JPA 的优势

- <mark>标准化</mark>：提供相同的 API，这保证了基于 JPA 开发的企业应用能够经过少量的修改就能够在不同的 JPA 框架下运行
- <mark>简单易用，集成方便</mark>：JPA 的主要目标之一就是提供更加简单的编程模型，在 JPA 框架下创建实体和创建 Java 类一样简单，只需要使用`javax.persistence.Entity`进行注释；JPA 的框架和接口也都非常简单
- <mark>可媲美 JDBC 的查询能力</mark>：JPA 的查询语言是面向对象的，JPA 定义了独特的 `JPQL`，而且能够支持批量更新和修改、JOIN、GROUP BY、HAVING 等通常只有 SQL 才能够提供的高级查询特性，甚至还能够支持子查询
- <mark>支持面向对象的高级特性</mark>：JPA 中能够支持面向对象的高级特性，如类之间的继承、多态和类之间的复杂关系，最大限度的使用面向对象的模型



## 6、JPA 技术

- <mark>ORM 映射元数据</mark>：JPA 支持 *XML* 和 *JDK5.0 注解* 两种元数据的形式，元数据描述对象和表之间的映射关系，框架据此将实体对象持久化到数据库表中
- <mark>JPA 的 API</mark>：用来操作实体对象，执行 CRUD 操作，框架在后台完成所有的事情，开发者从繁琐的 JDBC 和 SQL 代码中解脱出来
- <mark>查询语言（JPQL）</mark>：这是持久化操作中很重要的一个方面，通过面向对象而非面向数据库的查询语言查询数据，避免程序和具体的 SQL 紧密耦合



## 7、HelloWorld

### JPA 持久化对象步骤

1、<mark>创建 persistence.xml，配置持久化单元</mark>

- 需要指定跟哪个数据库进行交互
- 需要指定 JPA 使用哪个持久化的框架以及配置该框架的基本属性

2、<mark>创建实体类，使用 annotation 描述实体类与数据库表间映射关系</mark>

3、<mark>使用 JPA API 完成数据增删改查操作</mark>

- 创建 `EntityManagerFactory`（对应 Hibernate 中的 SessionFactory）
- 创建 `EntityManager`（对应 Hibernate 中的 Session）

### 1）创建 JPA 工程
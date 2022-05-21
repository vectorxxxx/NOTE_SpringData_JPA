> 笔记来源：[尚硅谷SpringData教程(springdata经典，spring data快速上手)](https://www.bilibili.com/video/BV1hW411g7jy)

[TOC]

# SpringData 简介

## 1、Spring Data 概述

Spring Data：Spring 的一个子项目。用于简化数据库访问，支持 *NoSQL* 和 *关系数据存储*。其主要目标是使数据库的访问变得方便快捷

SpringData 项目所支持 *NoSQL 存储*：

- MongoDB（文档数据库）
- Neo4j（图形数据库）
- Redis（键/值存储）
- Hbase（列族数据库）

SpringData 项目所支持的 *关系数据存储技术*：
- JDBC
- <mark>JPA</mark>



## 2、Spring Data JPA 概述

Spring Data JPA：致力于减少数据访问层（DAO）的开发量.开发者唯一要做的，就只是 *声明持久层的接口*，其他都交给 Spring Data JPA 来帮你完成！

框架怎么可能代替开发者实现业务逻辑呢？

比如：当有一个 `UserDao.findUserById` 这样一个方法声明，大致应该能判断出这是根据给定条件的 ID 查询出满足条件的 User 对象

Spring Data JPA 做的便是规范方法的名字，根据符合规范的名字来确定方法需要实现什么样的逻辑

```java
public interface PersonRepository extends Repository<Person,Integer>{
    Person getByLastName(String lastName);
}
```



## 3、HelloWorld

使用 Spring Data JPA 进行持久层开发需要的四个步骤：

1. <mark>配置 Spring 整合 JPA</mark>
2. <mark>在 Spring 配置文件中配置 Spring Data</mark>：让 Spring 为声明的接口创建代理对象。配置了 `<jpa:repositories>` 后，Spring 初始化容器时将会扫描 `base-package` 指定的包目录及其子目录，为继承 `Repository` 或其子接囗的接口创建代理对象，并将代理对象注册为 `Spring Bean`，业务层便可以通过 Spring 自动封装的特性来直接使用该对象
3. <mark>声明持久层的接口，该接口继承 Repository</mark>：`Repository` 是一个标记型接口，它不包含任何方法。如必要，Spring Data 可实现 `Repository` 其他子接口，其中定义了一些常用的增删改查，以及分页相关的方法
4. <mark>在接口中声明需要的方法</mark>：Spring Data 将根据给定的策略（具体筛略稍后讲解）来为其生成实现代码

### 3.1、开发准备

#### pom 依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.vectorx.springdata</groupId>
    <artifactId>SpringData</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <spring.version>4.0.0.RELEASE</spring.version>
    </properties>

    <dependencies>
        <!-- Spring 相关 -->
        <dependency>
            <groupId>net.sourceforge.cglib</groupId>
            <artifactId>com.springsource.net.sf.cglib</artifactId>
            <version>2.2.0</version>
        </dependency>
        <dependency>
            <groupId>org.aopalliance</groupId>
            <artifactId>com.springsource.org.aopalliance</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>com.springsource.org.aspectj.weaver</artifactId>
            <version>1.6.4.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.1.3</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-expression</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <!-- Hibernate 相关 -->
        <dependency>
            <groupId>antlr</groupId>
            <artifactId>antlr</artifactId>
            <version>2.7.7</version>
        </dependency>
        <dependency>
            <groupId>dom4j</groupId>
            <artifactId>dom4j</artifactId>
            <version>1.6.1</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.common</groupId>
            <artifactId>hibernate-commons-annotations</artifactId>
            <version>4.0.2.Final</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>4.2.4.Final</version>
        </dependency>
        <dependency>
            <groupId>org.javassist</groupId>
            <artifactId>javassist</artifactId>
            <version>3.15.0-GA</version>
        </dependency>
        <dependency>
            <groupId>org.jboss.logging</groupId>
            <artifactId>jboss-logging</artifactId>
            <version>3.1.0.GA</version>
        </dependency>
        <dependency>
            <groupId>org.jboss.spec.javax.transaction</groupId>
            <artifactId>jboss-transaction-api_1.1_spec</artifactId>
            <version>1.0.1.Final</version>
        </dependency>
        <!-- JPA 相关 -->
        <dependency>
            <groupId>org.hibernate.javax.persistence</groupId>
            <artifactId>hibernate-jpa-2.0-api</artifactId>
            <version>1.0.1.Final</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>4.2.4.Final</version>
        </dependency>
        <!-- c3p0 相关 -->
        <dependency>
            <groupId>com.mchange</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.2.1</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-c3p0</artifactId>
            <version>4.2.4.Final</version>
        </dependency>
        <dependency>
            <groupId>com.mchange</groupId>
            <artifactId>mchange-commons-java</artifactId>
            <version>0.2.3.4</version>
        </dependency>
        <!-- MySQL 驱动相关 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>
        <!--SpringData 相关-->
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-commons</artifactId>
            <version>1.6.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
            <version>1.4.2.RELEASE</version>
        </dependency>
        <!-- junit 相关 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

#### 数据库配置

```properties
jdbc.user=root
jdbc.password=root
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.jdbcUrl=jdbc:mysql:///jpa
```

#### Spring 配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
            http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
            http://www.springframework.org/schema/data/jpa  http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">
    <!--1、配置 c3p0 数据源-->
    <context:property-placeholder location="classpath:db.properties"/>
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="user" value="${jdbc.user}"></property>
        <property name="password" value="${jdbc.password}"></property>
        <property name="driverClass" value="${jdbc.driverClass}"></property>
        <property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
    </bean>
    <!--2、配置 EntityManagerFactory-->
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="dataSource" ref="dataSource"></property>
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"></bean>
        </property>
        <property name="packagesToScan" value="com.vectorx.springdata.entities"></property>
        <property name="jpaProperties">
            <props>
                <!--生成数据表列的映射策略-->
                <prop key="hibernate.ejb.naming_strategy">org.hibernate.cfg.ImprovedNamingStrategy</prop>
                <!--hibernate 基本属性-->
                <prop key="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</prop>
                <prop key="hibernate.show_sql">true</prop>
                <prop key="hibernate.format_sql">true</prop>
                <prop key="hibernate.hbm2ddl.auto">update</prop>
            </props>
        </property>
    </bean>
    <!--3、配置 事务管理器-->
    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <!--配置 EntityManagerFactory-->
        <property name="entityManagerFactory" ref="entityManagerFactory"></property>
    </bean>
    <!--4、配置 基于注解的事务-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
    <!--5、配置 SpringData-->
    <jpa:repositories base-package="com.vectorx.springdata" entity-manager-factory-ref="entityManagerFactory"></jpa:repositories>
</beans>
```

### 3.2、编写代码

#### 实体类

```java
@Table(name = "JPA_PERSONS")
@Entity
public class Person
{
    private Integer id;
    private String lastName;
    private String email;
    private Date birthDay;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", birthDay=" + birthDay + '}';
    }
}
```

#### Repository 类

```java
public interface PersonRepository extends Repository<Person, Integer>
{
    Person getByLastName(String lastName);
}
```

### 3.3、测试代码

1）测试数据源是否可用

```java
public class SpringDataTest
{
    private ApplicationContext context;
    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }
}
```

日志信息

```java
com.mchange.v2.c3p0.impl.NewProxyConnection@3419e23b
```

数据表生成情况

![image-20220521230339765](https://s2.loli.net/2022/05/21/RJv9ihqrAwbf2Qa.png)

2）插入一条测试数据备用

![image-20220521230404151](https://s2.loli.net/2022/05/21/7V581OetiQnuHJP.png)

3）测试 Repository 能否正常执行

```java
@Test
public void testPersonRepository(){
    PersonRepository repository = context.getBean(PersonRepository.class);
    Person person = repository.getByLastName("AA");
    System.out.println(person);
}
```

日志信息

```sql
Hibernate: 
    select
        person0_.id as id1_0_,
        person0_.birth_day as birth_da2_0_,
        person0_.email as email3_0_,
        person0_.last_name as last_nam4_0_ 
    from
        jpa_persons person0_ 
    where
        person0_.last_name=?
Person{id=1, lastName='AA', email='AA@qq.com', birthDay=2022-05-21 22:48:34.0}
```


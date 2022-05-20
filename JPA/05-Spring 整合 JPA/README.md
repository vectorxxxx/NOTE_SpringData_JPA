> 笔记来源：[尚硅谷jpa开发教程全套完整版(初学者零基础入门)](https://www.bilibili.com/video/BV1vW411M7zp)

[TOC]

# Spring 整合 JPA

## 1、三种整合方式

- 1）`LocalEntityManagerFactoryBean`：适用于那些仅使用 JPA 进行数据访问的项目，该 FactoryBean 将根据 JPAPersistenceProvider 自动检测配置文件进行工作，一般从 ”META-INF/persistence.xml“ 读取配置信息，这种方式最简单，但<mark>不能设置 Spring 中定义的 DataSource，且不支持 Spring 管理的全局事务</mark>
- 2）从 `JNDI` 中获取：用于<mark>从 JavaEE 服务器获取</mark>指定的 EntityManagerFactory，这种方式在进行 Spring 事务管理时一般要使用 JTA 事务管理
- 3）`LocalContainerEntityManagerFactoryBean`：适用于所有环境的 FactoryBean，能全面控制 EntityManagerFactory 配置，如指定 Spring  定义的 DataSource 等等

综上所述，第三种整合方式最优



## 2、pom 依赖

依赖 jar 包清单：

- Hibernate 相关
- JPA 相关
- c3p0 相关
- MySQL 驱动相关
- Spring 相关

```xml
<properties>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
    <spring.version>4.0.0.RELEASE</spring.version>
</properties>

<dependencies>
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
    <!-- MySQL 相关 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.28</version>
    </dependency>
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
    <!-- junit 相关 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```



## 3、jdbc 配置

`resouces` 目录下新建文件 `db.properties`，配置信息如下

```properties
jdbc.user=root
jdbc.password=root
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.jdbcUrl=jdbc:mysql:///jpa
```



## 4、Spring 配置

`resouces` 目录下新建文件 `applicationContext.xml`，配置清单如下

- 自动扫描包
- c3p0 数据源
- DataSource
- EntityManagerFactory
- 事务管理器
- 基于注解的事务

配置信息如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
            http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">
    <!--配置自动扫描的包-->
    <context:component-scan base-package="com.vectorx.jpa_spring"></context:component-scan>
    <!--配置 c3p0 数据源-->
    <context:property-placeholder location="classpath:db.properties"/>
    <!--配置 DataSource-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="user" value="${jdbc.user}"></property>
        <property name="password" value="${jdbc.password}"></property>
        <property name="driverClass" value="${jdbc.driverClass}"></property>
        <property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
    </bean>
    <!--配置 EntityManagerFactory-->
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <!--配置 DataSource-->
        <property name="dataSource" ref="dataSource"></property>
        <!--配置 JPA 提供商适配器-->
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"></bean>
        </property>
        <!--配置实体类所在的包-->
        <property name="packagesToScan" value="com.vectorx.jpa_spring.entities"></property>
        <!--配置 JPA 基本属性-->
        <property name="jpaProperties">
            <props>
                <prop key="hibernate.show_sql">true</prop>
                <prop key="hibernate.format_sql">true</prop>
                <prop key="hibernate.hbm2ddl.auto">update</prop>
            </props>
        </property>
    </bean>
    <!--配置 JPA 的事务管理器-->
    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <!--配置 EntityManagerFactory-->
        <property name="entityManagerFactory" ref="entityManagerFactory"></property>
    </bean>
    <!--配置支持基于注解的事务-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```



## 5、HelloWorld

### 准备工作

Person 实体类

```java
@Table(name = "JPA_PERSONS")
@Entity
public class Person
{
    private Integer id;
    private String lastName;
    private String email;
    private int age;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "LAST_NAME")
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

PersonDao 类

```java
@Repository
public class PersonDao
{
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Person p){
        entityManager.persist(p);
    }
}
```

PersonService 类

```java
@Service
public class PersonService
{
    @Autowired
    private PersonDao personDao;

    @Transactional
    public void savePerson(Person p1, Person p2) {
        personDao.save(p1);
        personDao.save(p2);
    }
}
```

### 初始化表

```java
public class JPATest
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

执行 testDataSource 测试方法，查看日志信息，能够打印出连接对象信息，说明 DataSource 数据源配置正常

```java
com.mchange.v2.c3p0.impl.NewProxyConnection@71104a4
```

查看 JPA_PERSONS 数据表结构也正常生成

![image-20220521000214313](https://s2.loli.net/2022/05/21/2cLSZVI3HU6qOeG.png)

### 测试保存

```java
public class JPATest
{
    private ApplicationContext context;
    private PersonService personService;
    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        personService = context.getBean(PersonService.class);
    }

    @Test
    public void testPersonService(){
        Person p1 = new Person();
        p1.setLastName("AA");
        p1.setAge(11);
        p1.setEmail("AA@qq.com");
        Person p2 = new Person();
        p2.setLastName("BB");
        p2.setAge(22);
        p2.setEmail("BB@qq.com");

        System.out.println(personService.getClass().getName());
        personService.savePerson(p1, p2);
    }
}
```

日志信息

```sql
com.vectorx.jpa_spring.service.PersonService$$EnhancerByCGLIB$$2e5519cd
Hibernate: 
    insert 
    into
        JPA_PERSONS
        (age, email, LAST_NAME) 
    values
        (?, ?, ?)
Hibernate: 
    insert 
    into
        JPA_PERSONS
        (age, email, LAST_NAME) 
    values
        (?, ?, ?)
```

数据表 `JPA_PERSONS` 数据

![image-20220520234157301](https://s2.loli.net/2022/05/20/vUlQTdngqXYwL26.png)

修改 PersonService 的 savePerson 方法，模拟异常的发生

```java
personDao.save(p1);
// 模拟异常
int i = 10 / 0;
personDao.save(p2);
```

再次执行测试类方法，查看日志信息

```java
com.vectorx.jpa_spring.service.PersonService$$EnhancerByCGLIB$$cab2539e
Hibernate: 
    insert 
    into
        JPA_PERSONS
        (age, email, LAST_NAME) 
    values
        (?, ?, ?)

java.lang.ArithmeticException: / by zero

	at com.vectorx.jpa_spring.service.PersonService.savePerson(PersonService.java:26)
	at com.vectorx.jpa_spring.service.PersonService$$FastClassByCGLIB$$5fe473d0.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)
	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:713)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157)
	at org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:98)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:262)
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:95)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:646)
	at com.vectorx.jpa_spring.service.PersonService$$EnhancerByCGLIB$$cab2539e.savePerson(<generated>)
	at com.vectorx.jpa_spring.JPATest.testPersonService(JPATest.java:46) <25 internal lines>
```

说明事务生效，符合预期


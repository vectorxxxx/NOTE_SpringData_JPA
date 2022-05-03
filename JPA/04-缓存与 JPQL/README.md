> 笔记来源：[尚硅谷jpa开发教程全套完整版(初学者零基础入门)](https://www.bilibili.com/video/BV1vW411M7zp)

[TOC]

# 缓存与 JPQL

## 1、缓存

### 1.1、一级缓存

**测试方法 1**

```java
@Test
public void testSecondaryCache(){
    Customer customer1 = entityManager.find(Customer.class, 1);
    Customer customer2 = entityManager.find(Customer.class, 1);
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_2_0_,
        customer0_.age as age2_2_0_,
        customer0_.birthDay as birthDay3_2_0_,
        customer0_.createTime as createTi4_2_0_,
        customer0_.email as email5_2_0_,
        customer0_.LAST_NAME as LAST_NAM6_2_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
```

由于 JPA 的一级缓存，所以实际上只发送了一条 SQL

**测试方法 2**

```java
@Test
public void testSecondaryCache(){
    Customer customer1 = entityManager.find(Customer.class, 1);

    entityTransaction.commit();
    entityManager.close();

    entityManager = entityManagerFactory.createEntityManager();
    entityTransaction = entityManager.getTransaction();
    entityTransaction.begin();

    Customer customer2 = entityManager.find(Customer.class, 1);
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_2_0_,
        customer0_.age as age2_2_0_,
        customer0_.birthDay as birthDay3_2_0_,
        customer0_.createTime as createTi4_2_0_,
        customer0_.email as email5_2_0_,
        customer0_.LAST_NAME as LAST_NAM6_2_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
Hibernate: 
    select
        customer0_.id as id1_2_0_,
        customer0_.age as age2_2_0_,
        customer0_.birthDay as birthDay3_2_0_,
        customer0_.createTime as createTi4_2_0_,
        customer0_.email as email5_2_0_,
        customer0_.LAST_NAME as LAST_NAM6_2_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
```

这次由于 entityManager 是重新获取的，一级缓存中的内容已经被清理掉了，所以发送了两条 SQL

而 *二级缓存* 的意义就在于可以跨 JPA 中的 EntityManager，是上述方法可以是发送一条 SQL

### 1.2、二级缓存

在使用二级缓存前需要做以下准备工作

`pom.xml` 中添加 *二级缓存* 相关依赖

```xml
<!-- 二级缓存相关依赖 -->
<dependency>
    <groupId>net.sf.ehcache</groupId>
    <artifactId>ehcache-core</artifactId>
    <version>2.4.3</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-ehcache</artifactId>
    <version>4.2.4.Final</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.6.1</version>
</dependency>
```

`persistence.xml` 中添加 *二级缓存* 相关配置

```xml
<persistence version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">
    <persistence-unit name="jpa-1" transaction-type="RESOURCE_LOCAL">
        <!--略...-->
        <!--配置二级缓存策略
            ALL：所有的实体类都被缓存
            NONE：所有的实体类都不被缓存
            ENABLE_SELECTIVE：标识 @Cacheable(true) 注解的实体类将被缓存
            DISABLE_SELECTIVE：除标识 @Cacheable(false) 注解以外的所有实体类都将被缓存
            UNSPECIFIED：默认值，JPA 产品默认值将被使用
        -->
        <shared-cache-mode>ENABLE_SELECTIVE</shared-cache-mode>
        <properties>
            <!--略...-->
            <!--二级缓存相关-->
            <property name="hibernate.cache.use_second_level_cache" value="true"/>
            <property name="hibernate.cache.region.factory_class" value="org.hibernate.cache.ehcache.EhcacheRegionFactory"/>
            <property name="hibernate.cache.use_query_cache" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

`Customer.java` 中添加 *二级缓存* 相关注解

```java
@Cacheable(value = true)
@Entity
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    // ...
}
```

再次运行上述代码，查看日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_2_0_,
        customer0_.age as age2_2_0_,
        customer0_.birthDay as birthDay3_2_0_,
        customer0_.createTime as createTi4_2_0_,
        customer0_.email as email5_2_0_,
        customer0_.LAST_NAME as LAST_NAM6_2_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
```



## 2、JPQL

### 2.1、JPQL 语言

- `JPQL` 语言，即 Java Persistence Query language 的简称
- `JPQL` 是一种和 SQL 非常类似的中间性和对象化查询语言，它最终会被编译成针对不同底层数据库的 SQL 查询，从而屏蔽不同数据库的差异
- `JPQL` 语言的语句可以是 select 语句、update 语句或 delete 语句，它们都通过 `Query` 接口封装执行

### 2.2、Query 接口

`Query` 接口封装了执行数据库查询的相关方法调用 EntityManager 的 `createQuery`、`createNamedQuery` 及 `createNativeQuery` 方法可以获得查询对象，进而可调用 `Query` 接口的相关方法来执行查询操作

`Query` 接口的主要方法

- `int executeUpdate()` 用于执行 update 或 delete 语句
- `List getResultList()` 用于执行 select 语句并返回结果集实体列表
- `Object getsingleResult()` 用于执行只返回单个结果实体的 select 语句
- `Query setFirstResult(int startPosition)` 用于设置从哪个实体记录开始返回查询结果
- `Query setMaxResults(int maxResult)` 用于设置返回结果实体的最大数。与 `setFirstResult` 结合使用可实现分页查询
- `Query setFlushMode(FlushModeType flushMode)` 设置查询对象的 Flush 模式。参数可以取 2 个枚举值
  - `FlushModeType.AUTO` 为自动更新数据库记录
  - `FlushModeType.COMMMIT` 为直到提交事务时才更新数据库记录
- `setHint(String hintName, Object value)` 设置与查询对象相关的特定供应商参数或提示信息。参数名及其取值需要参考特定 JPA 实现库提供商的文档。如果第二个参数无效将抛出 IllegalArgumentException 异常
- `setParameter(int position, Object value)` 为查询语句的指定位置参数赋值。position 指定参数序号，value 为赋给参数的值
- `setParameter(int position, Date d, TemporalType type)` 为查询语句的指定位置参数赋 Date 值。position 指定参数序号，value 为赋给参数的值，type 取 TemporalType 的枚举常量，包括 DATE、TIME 及 TIMESTAMP 三个，用于将 Java 的 Date 型值临时转换为数据库支持的日期时间类型（java.sql.Date、iava.sql.Time 及 java.sql.Timestamp）

### 2.3、select 语句

`select` 语句用于执行查询。其语法可表示为

```sql
select clause form_clause
	[where_clause]
	[groupby_clause]
	[having_clause]
	[orderby_clause]
```

### 2.4、from 子句

`from` 子句是查询语句的必选子句

- select 用来指定查询返回的结果实体或实体的某些属性
- from 子句声明查询源实体类，并指定标识符变量（相当于 SQL 表的别名）。如果不希望返回重复实体，可使用关键字 distinct 修饰。select、from 都是 JPQL 的关键字，通常全大写或全小写，建议不要大小写混用

### 2.5、查询所有实体

查询所有实体的 JPQL 查询字串很简单，例如：`select o from Order o` 或 `select o from Orders as o`

关键字 as 可以省去。标识符变量的命名规范与 Java 标识符相同，且区分大小写

调用 EntityManager 的 `createQuery()` 方法可创建查询对象，接着调用 Query 接囗的 `getResultList()` 方法就可获得查询结果集。例如：

```java
Query query = entityManager.createQuery("select o from Order o");
List orders = query.getResultList();
Iterator iterator = orders.iterator();
while(iterator.hasNext()){
    //处理 Order
}
```

### 2.6、where 子句

`where` 子句用于指定查询条件，`where` 跟条件表达式。例：

```sql
select o from Orders o where o.id = 1;
select o from Orders o where o.id > 3 and o.confirm = 'true';
select o from Orders o where o.address.streetNumber >= 123;
```

JPQL 也支持包含参数的查询，例如：

```sql
select o from Orders o where o.id = :myId;
select o from Orders o where o.id = :myId and o.customer = :customerName;
```

> **注意**：参数名前必须冠以冒号(:)，执行查询前须使用 `Query.setParameter(name, value)` 方法给参数赋值

也可以不使用参数名而使用参数的序号，例如：

```sql
select o from Order o where o.id=?1 and o.customer=?2
```

其中 `?1` 代表第一个参数，`?2` 代表第二个参数。在执行查询之前需要使用重载方法 `Query.setParameter(pos，value)` 提供参数值

```sql
Query query = entityManager.createQuery("select o from Orders o where o.id=?1 and o.customer=?2");
query.setParameter(1, 2);
query.setParameter(2, "John");
List orders = query.getResultList();
```

`where` 条件表达式中可用的运算符基本上与 SQL 一致，包括

- 算术运算符：`+`、`-`、`*`、`/`、`+`（正）、`-`（负）
- 关系运算符：`==`、`<>`、`>`、`>=`、`<`、`<=`、`between...and`、`like`、`in`、`is null` 等
- 逻辑运算符：`and`、`or`、`not`

下面是一些常见查询表达式示例：

```sql
# 以下语句查询 Id 介于 100至 200 之间的订单。
select o from Orders o where o.id between 100 and 200 
# 以下语句查询国籍为的 'US'、'CN' 或 'JP' 的客户
select c from Customers c where c.county in("US'，'CN"，"JP")
# 以下语句查询手机号以 139 开头的客户。% 表示任意多个字符序列，包括 0 个。
select c from Customers c where c.phone like '139%'
# 以下语句查询名字包含 4 个字符，且 234 位为 ose 的客户。_ 表示任意单个字符。
select c from Customers c where c.lname like '_ose'
# 以下语句查询电话号码未知的客户。Null 用于测试单值是否为空。
select c from Customers c where c.phone is null
# 以下语句查询尚未输入订单项的订单。empty 用于测试集合是否为空。
select o from Orders o where o.orderltems is empty
```

### 2.7、查询部分属性

如果只须查询实体的部分属性而不需要返回整个实体。例如：

```sql
select o.id, o.customerName, o.address.streetNumber from Order o order by o.id
```

执行该查询返回的不再是 Orders 实体集合，而是一个对象数组的集合（Object[]），集合的每个成员为一个对象数组，可通过数组元素访问各个属性



## 3、JPQL 之 HelloWorld

### 3.1、where 子句

```java
@Test
public void testAll(){
    String sql = "from Customer c where c.age > ?";
    Query query = entityManager.createQuery(sql);
    // 占位符索引是从 1 开始的
    query.setParameter(1, 1);
    List resultList = query.getResultList();
    System.out.println(resultList.size());
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_2_,
        customer0_.age as age2_2_,
        customer0_.birthDay as birthDay3_2_,
        customer0_.createTime as createTi4_2_,
        customer0_.email as email5_2_,
        customer0_.LAST_NAME as LAST_NAM6_2_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.age>?
1
```

### 3.2、查询部分属性

**测试方法 1**

```java
@Test
public void testPart() {
    String sql = "select c.lastName, c.age from Customer c where c.id = ?";
    List resultList = entityManager.createQuery(sql).setParameter(1, 1).getResultList();
    System.out.println(resultList);
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.LAST_NAME as col_0_0_,
        customer0_.age as col_1_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
[[Ljava.lang.Object;@2e77b8cf]
```

默认情况下，若只查询部分属性，则将返回 `Object[]` 类型的结果。或者 `Object[]` 类型的 `List`

也可以在实体类中创建对应的构造器，然后在 JPQL 语句中利用对应的构造器返回实体类的对象

```java
@Entity
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    // ...
    public Customer() {
    }

    public Customer(String lastName, int age) {
        this.lastName = lastName;
        this.age = age;
    }
    // ...
}
```

**测试方法 2**

```java
@Test
public void testPart2() {
    String sql = "select new Customer(c.lastName, c.age) from Customer c where c.id = ?";
    List resultList = entityManager.createQuery(sql).setParameter(1, 1).getResultList();
    System.out.println(resultList);
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.LAST_NAME as col_0_0_,
        customer0_.age as col_1_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
[Customer{id=null, lastName='Vector', email='null', age=100, birthDay=null, createTime=null}]
```

可以看到，在 JPQL 中使用 `new Customer(c.lastName, c.age)` 查询部分属性时，得到的返回类型也变成了 `Customer`

### 3.3、createNamedQuery

使用 `@NamedQuery` 注解定义相关 JPQL 语句

```java
@NamedQuery(name = "testNamedQuery", query = "select c from Customer c where c.id = ?")
@Entity
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    //...
}
```

**测试方法**

```java
@Test
public void testNamedQuery(){
    Customer customer = (Customer)entityManager.createNamedQuery("testNamedQuery").setParameter(1, 1).getSingleResult();
    System.out.println(customer);
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_2_,
        customer0_.age as age2_2_,
        customer0_.birthDay as birthDay3_2_,
        customer0_.createTime as createTi4_2_,
        customer0_.email as email5_2_,
        customer0_.LAST_NAME as LAST_NAM6_2_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
Customer{id=1, lastName='Vector', email='vector@qq.com', age=100, birthDay=2022-05-03, createTime=2022-05-03 17:20:24.0}
```

### 3.4、createNativeQuery

```java
@Test
public void testNativeQuery(){
    String sql = "select age from jpa_customers where id=?";
    Object result = entityManager.createNativeQuery(sql).setParameter(1, 1).getSingleResult();
    System.out.println(result);
}
```

日志信息

```sql
Hibernate: 
    select
        age 
    from
        jpa_customers 
    where
        id=?
100
```



## 4、JPQL 使用 Hibernate 的查询缓存

> **注意**：需要在 `persistence.xml` 配置文件中开启缓存相关配置，与 *二级缓存* 相关配置基本一致，唯一不同的是只需要以下配置
>
> `<property name="hibernate.cache.use_second_level_cache" value="true"/>`
>
> `<property name="hibernate.cache.region.factory_class" value="org.hibernate.cache.ehcache.EhCacheRegionFactory"/>`
>
> `<property name="hibernate.cache.use_query_cache" value="true"/>`
>
> 而  `<shared-cache-mode>ENABLE_SELECTIVE</shared-cache-mode>` 和 `@Cacheable` 就不再需要了

**测试方法 1**：默认情况下多次查询会发送多条 SQL

```java
@Test
public void testQueryCache(){
    String sql = "select c from Customer c where c.id=?";
    // First Query
    Query query = entityManager.createQuery(sql).setParameter(1, 1);
    List resultList = query.getResultList();
    System.out.println(resultList.size());
    // Second Query
    Query query2 = entityManager.createQuery(sql).setParameter(1, 1);
    List resultList2 = query2.getResultList();
    System.out.println(resultList2.size());
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_2_,
        customer0_.age as age2_2_,
        customer0_.birthDay as birthDay3_2_,
        customer0_.createTime as createTi4_2_,
        customer0_.email as email5_2_,
        customer0_.LAST_NAME as LAST_NAM6_2_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
1
Hibernate: 
    select
        customer0_.id as id1_2_,
        customer0_.age as age2_2_,
        customer0_.birthDay as birthDay3_2_,
        customer0_.createTime as createTi4_2_,
        customer0_.email as email5_2_,
        customer0_.LAST_NAME as LAST_NAM6_2_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
1
```

通过调用 `setHint()` 方法设置 `HINT_CACHEABLE` 为 true，即开启查询缓存

```java
@Test
public void testQueryCache(){
    String sql = "select c from Customer c where c.id=?";
    // First Query
    Query query = entityManager.createQuery(sql).setParameter(1, 1).setHint(QueryHints.HINT_CACHEABLE, true);
    List resultList = query.getResultList();
    System.out.println(resultList.size());
    // Second Query
    Query query2 = entityManager.createQuery(sql).setParameter(1, 1).setHint(QueryHints.HINT_CACHEABLE, true);
    List resultList2 = query2.getResultList();
    System.out.println(resultList2.size());
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_2_,
        customer0_.age as age2_2_,
        customer0_.birthDay as birthDay3_2_,
        customer0_.createTime as createTi4_2_,
        customer0_.email as email5_2_,
        customer0_.LAST_NAME as LAST_NAM6_2_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
1
1
```

可以看到，原本的 2 条 SQL 语句只剩下了 1 条



## 5、JPQL 的 Order By 和 Group By

**测试方法 1**

```java
@Test
public void testOrderBy(){
    String sql = "select c from Customer c where c.id=? order by c.age desc";
    List resultList = entityManager.createQuery(sql).setParameter(1, 1).getResultList();
    System.out.println(resultList.size());
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_2_,
        customer0_.age as age2_2_,
        customer0_.birthDay as birthDay3_2_,
        customer0_.createTime as createTi4_2_,
        customer0_.email as email5_2_,
        customer0_.LAST_NAME as LAST_NAM6_2_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=? 
    order by
        customer0_.age desc
1
```

**测试方法 2**

```java
@Test
public void testGroupBy(){
    String sql = "select o.customer from Order o group by o.customer having count(o.id) >= 2";
    List<Order> resultList = entityManager.createQuery(sql).getResultList();
    System.out.println(resultList);
}
```

日志信息

```sql
Hibernate: 
    select
        customer1_.id as id1_2_,
        customer1_.age as age2_2_,
        customer1_.birthDay as birthDay3_2_,
        customer1_.createTime as createTi4_2_,
        customer1_.email as email5_2_,
        customer1_.LAST_NAME as LAST_NAM6_2_ 
    from
        JPA_ORDER order0_ 
    inner join
        JPA_CUSTOMERS customer1_ 
            on order0_.CUSTOMER_ID=customer1_.id 
    group by
        order0_.CUSTOMER_ID 
    having
        count(order0_.id)>=2
[Customer{id=4, lastName='customer2', email='customer2@qq.com', age=21, birthDay=2022-05-03, createTime=2022-05-03 20:41:55.0}]
```



## 6、JPQL 的关联查询

在 JPQL 中，很多时候都是通过在实体类中配置实体关联的类属性来实现隐含的关联（join）查询。例如：

```sql
select o from Orders o where o.address.streetNumber = 2000
```

上述 JPQL 语句编译成以下 SQL 时就会自动包含关联，默认为左关联

在某些情况下可能仍然需要对关联做精确的控制。为此，JPQL 也支持和 SQL 中类似的关联语法。如：

- `left out join` / `left join`
- `inner join`
- `left join` / `inner join fetch`

其中，`left join` 和 `left out join` 等义，都是允许符合条件的右边表达式中的实体为空


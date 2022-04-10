> 笔记来源：[尚硅谷jpa开发教程全套完整版(初学者零基础入门)](https://www.bilibili.com/video/BV1vW411M7zp)

[TOC]

# JPA API

## 1、Persistence

`Persistence` 类是用于获取 `EntityManagerFactory` 实例

该类包含一个名为 `createEntityManagerFactory` 的静态方法，它有如下两个重载方法

- 带有一个参数的方法：以 JPA 配置文件 `persistence.xml` 中的持久化单元名为参数

```java
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
```

- 带有两个参数的方法：前一个参数含义相同；后一个参数 Map 类型，用于设置 JPA 的相关属性，这时将忽略其它地方设置的属性。Map 对象的属性名必须是 JPA 实现库提供商的名字空间约定的属性名

```java
Map<String, Object> properties = new HashMap<>();
properties.put("hibernate.show_sql", false);
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
```

此时执行 Main 方法，将不会打印 SQL 相关信息



## 2、EntityManagerFactory

`EntityManagerFactory` 接口主要用来创建 `EntityManager` 实例。该接口约定了如下 4 个方法：

- `createEntityManager()`：用于创建实体管理器对象实例
- `createEntityManager(Map map)`：用于创建实体管理器对象实例的重载方法，Map 参数用于提供 `EntityManager` 的属性
- `isOpen()`：检查 `EntityManagerFactory` 是否处于打开状态。实体管理器工厂创建后一直处于打开状态，除非调用 `close()` 方法将其关闭
- `close()`：关闭 `EntityManagerFactory`。`EntityManagerFactory` 关闭后将释放所有资源，`isOpen()` 方法测试将返回 false，其它方法将不能调用，否则将导致 IllegalStateException 异常

测试代码：我们分别在 `EntityManagerFactory` 实例创建后及关闭 `EntityManagerFactory` 后打印其 `isOpen()` 结果

```java
EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
System.out.println(entityManagerFactory.isOpen());
// ...
entityManagerFactory.close();
System.out.println(entityManagerFactory.isOpen());
```

日志信息

```java
true
// ...
false
```



## 3、EntityManager

在 JPA 规范中，`EntityManager` 是完成持久化操作的核心对象。实体作为普通 Java 对象，只有在调用 `EntityManager` 将其持久化后才会变成持久化对象。`EntityManager` 对象在一组实体类与底层数据源之间进行 O/R 映射的管理

它可以用来管理和更新 Entity Bean，根据主键查找 Entity Bean，还可以通过 JPQL 语句查询实体

实体的状态：

- **新建状态**：新创建的对象，<mark>尚未拥有持久性主键</mark>
- **持久化状态**：已经拥有持久性主键并<mark>和持久化建立了上下文环境</mark>
- **游离状态**：拥有持久化主键，但是<mark>没有与持久化建立上下文环境</mark>
- **删除状态**：拥有持久化主键，已经和持久化建立上下文环境，但是<mark>从数据库中删除</mark>

准备工作

```java
public class JPATest {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
    }

    @After
    public void destory() {
        entityTransaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }
}
```

### 3.1、find()

`find(Class<T> entityClass，Obiect primaryKey)`：返回指定的 OID 对应的实体类对象

- 如果这个实体存在于当前的持久化环境，则返回一个被缓存的对象
- 否则会创建一个新的 Entity，并加载数据库中相关信息。若 OID 不存在于数据库中，则返回一个 null
- 第一个参数为被查询的<mark>实体类类型</mark>，第二个参数为待查找实体的<mark>主键值</mark>

```java
/**
 * 类似于 Hibernate 中 Session 的 get() 方法
 */
@Test
public void testFind() {
    Customer customer = entityManager.find(Customer.class, 2);
    System.out.println(customer.getClass().getName());
    System.out.println("-----------------------------");
    System.out.println(customer);
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_0_0_,
        customer0_.age as age2_0_0_,
        customer0_.birthDay as birthDay3_0_0_,
        customer0_.createTime as createTi4_0_0_,
        customer0_.email as email5_0_0_,
        customer0_.LAST_NAME as LAST_NAM6_0_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
com.vectorx.jpa.helloworld.Customer
-----------------------------
Customer{id=2, lastName='Vector2', email='vector2@qq.com', age=100, birthDay=2022-04-10, createTime=2022-04-10 21:06:19}
```

### 3.2、getReference()

`getReference(Class<T> entityClass, Object primaryKey)`：与 `find()` 方法类似，不同的是：

- 如果缓存中不存在指定的 Entity，EntityManager 会创建一个 Entity 类的代理，但是不会立即加载数据库中的信息，只有第一次真正使用此 Entity 的属性才加载
- 所以如果此 OID 在数据库不存在，`getReference()` 不会返回 null 值，而是抛出 `EntityNotFoundException`

```java
/**
 * 类似于 Hibernate 中 Session 的 load() 方法
 */
@Test
public void testGetReference() {
    Customer customer = entityManager.getReference(Customer.class, 2);
    System.out.println(customer.getClass().getName());
    System.out.println("-----------------------------");
    System.out.println(customer);
}
```

日志信息

```sql
com.vectorx.jpa.helloworld.Customer_$$_javassist_0
-----------------------------
Hibernate: 
    select
        customer0_.id as id1_0_0_,
        customer0_.age as age2_0_0_,
        customer0_.birthDay as birthDay3_0_0_,
        customer0_.createTime as createTi4_0_0_,
        customer0_.email as email5_0_0_,
        customer0_.LAST_NAME as LAST_NAM6_0_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
Customer{id=2, lastName='Vector2', email='vector2@qq.com', age=100, birthDay=2022-04-10, createTime=2022-04-10 21:06:19}
```

1）可以发现，`getReference()` 方法与 `find()` 方法的区别：

- `getReference()` 方法是在获取 Customer 对象的属性之后才打印 SQL；而 `find()` 方法是在获取 Customer 对象的属性之前就打印 SQL
- `getReference()` 方法打印的 Customer 对象是实例化后的对象；而 `find()` 方法打印的 Customer 对象是代理对象

```console
com.vectorx.jpa.helloworld.Customer
com.vectorx.jpa.helloworld.Customer_$$_javassist_0
```

这说明 `getReference()` 方法获取的实体对象只是其 *代理对象*，只有在真正使用实体对象的属性时，JPA 才会向数据库发送 SQL，初始化实体对象。因此，这就会导致有可能出现 *懒加载异常* 的问题

```java
@Test
public void testGetReference() {
    Customer customer = entityManager.getReference(Customer.class, 2);
    System.out.println(customer.getClass().getName());
    System.out.println("-----------------------------");
    entityTransaction.commit();
    entityManager.close();
    System.out.println(customer);
}
```

日志信息：抛出了 `LazyInitializationException` 异常

```java
org.hibernate.LazyInitializationException: could not initialize proxy - no Session

	at org.hibernate.proxy.AbstractLazyInitializer.initialize(AbstractLazyInitializer.java:164)
	at org.hibernate.proxy.AbstractLazyInitializer.getImplementation(AbstractLazyInitializer.java:285)
	at org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer.invoke(JavassistLazyInitializer.java:185)
	at com.vectorx.jpa.helloworld.Customer_$$_javassist_0.toString(Customer_$$_javassist_0.java)
	at java.lang.String.valueOf(String.java:2994)
	at java.io.PrintStream.println(PrintStream.java:821)
	at helloworld.JPATest.testGetReference(JPATest.java:54)
```

2）如果查询不到对应记录

```java
@Test
public void testGetReference() {
    Customer customer = entityManager.getReference(Customer.class, 6);
    System.out.println(customer);
}
```

日志信息：抛出 `EntityNotFoundException` 异常

```java
javax.persistence.EntityNotFoundException: Unable to find com.vectorx.jpa.helloworld.Customer with id 6
<5 internal lines>
    at com.vectorx.jpa.helloworld.Customer_$$_javassist_0.toString(Customer_$$_javassist_0.java)
	at java.lang.String.valueOf(String.java:2994)
	at java.io.PrintStream.println(PrintStream.java:821)
	at helloworld.JPATest.testGetReference(JPATest.java:55) <27 internal lines>
```

### 3.3、persist()

`persist(Object entity)`：用于将新创建的 Entity 纳入到 EntityManager 的管理。该方法执行后，传入 `persist()` 方法的 Entity 对象转换成持久化状态

- 如果传入 `persist()` 方法的 Entity 对象已经处于持久化状态，则 `persist()` 方法什么都不做

- 如果对删除状态的 Entity 进行 `persist()` 操作，会转换为持久化状态

- 如果对游离状态的实体执行 `persist()` 操作，可能会在 `persist()` 方法抛出 `EntityExistException`（也有可能是在 flush 或事务提交后抛出）

> **存疑点**：不知为何，我后面的测试结果是抛出了 `PersistenceException`，而不是 `EntityExistException`

```java
/**
 * 类似于 Hibernate 中 Session 的 save 方法，使对象有临时状态变为持久化状态
 */
@Test
public void testPersistence() {
    Customer customer = new Customer();
    customer.setLastName("Vector4");
    customer.setEmail("vector4@qq.com");
    customer.setAge(4);
    customer.setBirthDay(new Date());
    customer.setCreateTime(new Date());
    entityManager.persist(customer);
    System.out.println(customer.getId());
}
```

日志信息

```sql
Hibernate: 
    insert 
    into
        JPA_CUSTOMERS
        (age, birthDay, createTime, email, LAST_NAME) 
    values
        (?, ?, ?, ?, ?)
4
```

JPA_CUSTOMERS 表数据插入成功

![image-20220410212446625](https://s2.loli.net/2022/04/10/V4ncIYAFhdNMpO3.png)

1）和 Hibernate 中 Session 的 save 方法的不同之处在于：若对象有 id，则不能执行 insert 操作，而会抛出异常

```java
@Test
public void testPersistence() {
    Customer customer = new Customer();
    // ...
    customer.setId(5);
    entityManager.persist(customer);
}
```

日志信息：抛出 `PersistenceException` 异常

```java
javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: detached entity passed to persist: com.vectorx.jpa.helloworld.Customer
<4 internal lines>
	at helloworld.JPATest.testPersistence(JPATest.java:71) <27 internal lines>
Caused by: org.hibernate.PersistentObjectException: detached entity passed to persist: com.vectorx.jpa.helloworld.Customer <27 internal lines>
	... 28 more
```

### 3.4、remove()

`remove(Object entity)`：删除实例。如果实例是被管理的，即与数据库实体记录关联，则同时会删除关联的数据库记录

```java
/**
 * 类似于 Hibernate 中 Session 的 delete() 方法。把对象对应的记录从数据库中移除
 */
@Test
public void testRemove() {
    Customer customer = new Customer();
    customer.setId(4);
    entityManager.remove(customer);
}
```

日志信息：抛出了 `IllegalArgumentException` 异常，不能移除一个 *游离对象*

```java
java.lang.IllegalArgumentException: Removing a detached instance com.vectorx.jpa.helloworld.Customer#4
<6 internal lines>
    at helloworld.JPATest.testRemove(JPATest.java:82) <27 internal lines>
```

1）需要注意的是，该方法只能移除 持久化对象。而 Hibernate 中 Session 的 delete() 方法还可以移除 游离对象

怎么办呢？

```java
@Test
public void testRemove() {
    Customer customer = entityManager.find(Customer.class, 4);
    entityManager.remove(customer);
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_0_0_,
        customer0_.age as age2_0_0_,
        customer0_.birthDay as birthDay3_0_0_,
        customer0_.createTime as createTi4_0_0_,
        customer0_.email as email5_0_0_,
        customer0_.LAST_NAME as LAST_NAM6_0_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    where
        customer0_.id=?
Hibernate: 
    delete 
    from
        JPA_CUSTOMERS 
    where
        id=?
```

JPA_CUSTOMERS 表数据删除成功

![image-20220410212551748](https://s2.loli.net/2022/04/10/XZqfa5xVinrvO8S.png)

### 3.5、merge()

### 3.6、其他方法
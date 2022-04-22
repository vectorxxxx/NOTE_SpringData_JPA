> 笔记来源：[尚硅谷jpa开发教程全套完整版(初学者零基础入门)](https://www.bilibili.com/video/BV1vW411M7zp)

[TOC]

# 映射关联关系

## 1、单向多对一

Order 实体类（多）

```java
@Table(name = "JPA_ORDER")
@Entity
public class Order {
    private Integer id;
    private String orderName;
    private Customer customer;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ORDER_NAME")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @JoinColumn(name = "CUSTOMERS_ID")
    @ManyToOne
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
```

Customer 实体类（一）

```java
@Entity
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;

    private Date birthDay;
    private Date createTime;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "LAST_NAME", length = 50, nullable = false)
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

    @Temporal(TemporalType.DATE)
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
```

### 1.1、保存

**测试方法 1**：先保存 Customer 对象，后保存 Order 对象

```java
@Test
public void testPersistence() {
    Customer customer1 = new Customer();
    customer1.setLastName("customer1");
    customer1.setAge(21);
    customer1.setEmail("customer1@qq.com");
    customer1.setCreateTime(new Date());
    customer1.setBirthDay(new Date());

    Order order1 = new Order();
    order1.setOrderName("Customer1-Order1");
    Order order2 = new Order();
    order2.setOrderName("Customer1-Order2");

    // 设置关联关系
    order1.setCustomer(customer1);
    order2.setCustomer(customer1);

    entityManager.persist(customer1);
    entityManager.persist(order1);
    entityManager.persist(order2);
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
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMERS_ID, ORDER_NAME) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMERS_ID, ORDER_NAME) 
    values
        (?, ?)
```

JPA_CUSTOMERS 表数据

![image-20220414215018881](https://s2.loli.net/2022/04/14/SUTghZusn4wOYLj.png)

JPA_ORDERS 表数据

![image-20220414215108867](https://s2.loli.net/2022/04/14/pawb6zlSyLWITQF.png)

**测试方法 2**：先保存 Order 对象，后保存 Customer 对象

```java
@Test
public void testPersist2() {
    Customer customer2 = new Customer();
    customer2.setLastName("customer2");
    customer2.setAge(21);
    customer2.setEmail("customer2@qq.com");
    customer2.setCreateTime(new Date());
    customer2.setBirthDay(new Date());

    Order order1 = new Order();
    order1.setOrderName("Customer2-Order1");
    Order order2 = new Order();
    order2.setOrderName("Customer2-Order2");

    // 设置关联关系
    order1.setCustomer(customer2);
    order2.setCustomer(customer2);

    entityManager.persist(order1);
    entityManager.persist(order2);
    entityManager.persist(customer2);
}
```

日志信息

```sql
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMERS_ID, ORDER_NAME) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMERS_ID, ORDER_NAME) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        JPA_CUSTOMERS
        (age, birthDay, createTime, email, LAST_NAME) 
    values
        (?, ?, ?, ?, ?)
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMERS_ID=?,
        ORDER_NAME=? 
    where
        id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMERS_ID=?,
        ORDER_NAME=? 
    where
        id=?
```

JPA_CUSTOMERS 表数据

![image-20220414220301303](https://s2.loli.net/2022/04/14/kVrqpL3S6BsmOPI.png)

JPA_ORDERS 表数据

![image-20220414220337662](https://s2.loli.net/2022/04/14/CkAhGIwJOabF3fZ.png)

测试方法 1 和测试方法 2 的区别：

- insert 语句顺序不一致
- 方法 2 多了两条 update

> **建议**：在保存多对一的关联关系时，建议先保存 1 的一端，后保存 n 的一端，这样不会多出额外的 update 语句

### 1.2、获取

```java
@Test
public void testFind() {
    Order order = entityManager.find(Order.class, 1);
    System.out.println(order.getOrderName());
    System.out.println(order.getCustomer().getLastName());
}
```

日志信息

```sql
Hibernate: 
    select
        order0_.id as id1_1_1_,
        order0_.CUSTOMERS_ID as CUSTOMER3_1_1_,
        order0_.ORDER_NAME as ORDER_NA2_1_1_,
        customer1_.id as id1_0_0_,
        customer1_.age as age2_0_0_,
        customer1_.birthDay as birthDay3_0_0_,
        customer1_.createTime as createTi4_0_0_,
        customer1_.email as email5_0_0_,
        customer1_.LAST_NAME as LAST_NAM6_0_0_ 
    from
        JPA_ORDER order0_ 
    left outer join
        JPA_CUSTOMERS customer1_ 
            on order0_.CUSTOMERS_ID=customer1_.id 
    where
        order0_.id=?
Customer1-Order1
customer1
```

> **注意**：默认情况下，使用左外连接获取 n 的一端的对象和其关联的 1 的一端的对象

想要改变其默认的加载策略，可以修改 Order 实体类中 `@ManyToOne` 的 `fetch` 属性值为 `LAZY`

```java
@JoinColumn(name = "CUSTOMERS_ID")
// fetch 设置关联属性的默认加载策略
@ManyToOne(fetch = FetchType.LAZY)
public Customer getCustomer() {
    return customer;
}
```

再次测试，查看日志信息

```sql
Hibernate: 
    select
        order0_.id as id1_1_0_,
        order0_.CUSTOMERS_ID as CUSTOMER3_1_0_,
        order0_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_ORDER order0_ 
    where
        order0_.id=?
Customer1-Order1
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
customer1
```

### 1.3、删除

**删除 n 的一端的记录**

```java
@Test
public void testRemoveN() {
    Order order = entityManager.find(Order.class, 2);
    entityManager.remove(order);
}
```

日志信息

```sql
Hibernate: 
    select
        order0_.id as id1_1_0_,
        order0_.CUSTOMERS_ID as CUSTOMER3_1_0_,
        order0_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_ORDER order0_ 
    where
        order0_.id=?
Hibernate: 
    delete 
    from
        JPA_ORDER 
    where
        id=?
```

**删除 1 的一端的记录**

```java
@Test
public void testRemove1() {
    Customer customer = entityManager.find(Customer.class, 9);
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
ERROR: Cannot delete or update a parent row: a foreign key constraint fails (`jpa`.`jpa_order`, CONSTRAINT `FK_ly166u7lew35w5ap1s4ocb5l6` FOREIGN KEY (`CUSTOMERS_ID`) REFERENCES `jpa_customers` (`id`))

javax.persistence.RollbackException: Error while committing the transaction
<1 internal line>
	at helloworld.Many2OneTest.destory(Many2OneTest.java:30) <26 internal line>
Caused by: javax.persistence.PersistenceException: org.hibernate.exception.ConstraintViolationException: could not execute statement
	at org.hibernate.ejb.AbstractEntityManagerImpl.convert(AbstractEntityManagerImpl.java:1387)
	at org.hibernate.ejb.AbstractEntityManagerImpl.convert(AbstractEntityManagerImpl.java:1310)
	at org.hibernate.ejb.TransactionImpl.commit(TransactionImpl.java:80)
	... 27 more
Caused by: org.hibernate.exception.ConstraintViolationException: could not execute statement <19 internal line> 
	... 27 more
Caused by: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`jpa`.`jpa_order`, CONSTRAINT `FK_ly166u7lew35w5ap1s4ocb5l6` FOREIGN KEY (`CUSTOMERS_ID`) REFERENCES `jpa_customers` (`id`))
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:117)
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122)
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:953)
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeUpdateInternal(ClientPreparedStatement.java:1098)
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeUpdateInternal(ClientPreparedStatement.java:1046)
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeLargeUpdate(ClientPreparedStatement.java:1371)
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeUpdate(ClientPreparedStatement.java:1031) <1 internal line>
	... 41 more
```

> **注意**：不能直接删除 1 的一端，因为有外键约束

### 1.4、更新

```java
@Test
public void testUpdate() {
    Order order = entityManager.find(Order.class, 1);
    order.getCustomer().setLastName("Manny2One-Update");
}
```

日志信息

```sql
Hibernate: 
    select
        order0_.id as id1_1_0_,
        order0_.CUSTOMERS_ID as CUSTOMER3_1_0_,
        order0_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_ORDER order0_ 
    where
        order0_.id=?
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
    update
        JPA_CUSTOMERS 
    set
        age=?,
        birthDay=?,
        createTime=?,
        email=?,
        LAST_NAME=? 
    where
        id=?
```

JPA_CUSTOMERS 表数据

![image-20220414223513252](https://s2.loli.net/2022/04/14/jiuQTo3Mg1sK5Nc.png)



## 2、单向一对多

Customer 实体类（一）

```java
@Entity
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;

    private Date birthDay;
    private Date createTime;

    private Set<Order> orders = new HashSet<>();

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "LAST_NAME", length = 50, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
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

    @Temporal(TemporalType.DATE)
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    // @JoinColumn 映射外键列名称
    @JoinColumn(name = "CUSTOMER_ID")
    // @OneToMany 映射 1-n 的关联关系
    @OneToMany
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
```

Order 实体类（多）

```java
@Table(name = "JPA_ORDER")
@Entity
public class Order {
    private Integer id;
    private String orderName;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ORDER_NAME")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}
```

### 2.1、保存

**测试方法 1**：先保存 Customer 对象，后保存 Order 对象

```java
@Test
public void testPersist() {
    Customer customer1 = new Customer();
    customer1.setLastName("customer1");
    customer1.setAge(11);
    customer1.setEmail("customer1@qq.com");
    customer1.setCreateTime(new Date());
    customer1.setBirthDay(new Date());

    Order order1 = new Order();
    order1.setOrderName("Customer1-Order1");
    Order order2 = new Order();
    order2.setOrderName("Customer1-Order2");

    // 设置关联关系
    Set<Order> orders = customer1.getOrders();
    orders.add(order1);
    orders.add(order2);

    entityManager.persist(customer1);
    entityManager.persist(order1);
    entityManager.persist(order2);
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
Hibernate: 
    insert 
    into
        JPA_ORDER
        (ORDER_NAME) 
    values
        (?)
Hibernate: 
    insert 
    into
        JPA_ORDER
        (ORDER_NAME) 
    values
        (?)
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=? 
    where
        id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=? 
    where
        id=?
```

**测试方法 2**：先保存 Order 对象，后保存 Customer 对象

```java
@Test
public void testPersist2() {
    Customer customer2 = new Customer();
    customer2.setLastName("customer2");
    customer2.setAge(22);
    customer2.setEmail("customer2@qq.com");
    customer2.setCreateTime(new Date());
    customer2.setBirthDay(new Date());

    Order order1 = new Order();
    order1.setOrderName("Customer2-Order1");
    Order order2 = new Order();
    order2.setOrderName("Customer2-Order2");

    // 设置关联关系
    Set<Order> orders = customer2.getOrders();
    orders.add(order1);
    orders.add(order2);

    entityManager.persist(order1);
    entityManager.persist(order2);
    entityManager.persist(customer2);
}
```

日志信息

```sql
Hibernate: 
    insert 
    into
        JPA_ORDER
        (ORDER_NAME) 
    values
        (?)
Hibernate: 
    insert 
    into
        JPA_ORDER
        (ORDER_NAME) 
    values
        (?)
Hibernate: 
    insert 
    into
        JPA_CUSTOMERS
        (age, birthDay, createTime, email, LAST_NAME) 
    values
        (?, ?, ?, ?, ?)
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=? 
    where
        id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=? 
    where
        id=?
```

JPA_CUSTOMERS 表数据

![image-20220422001637956](https://s2.loli.net/2022/04/22/YqXoGeMOg9CWTSt.png)

JPA_ORDERS 表数据

![image-20220422001713337](https://s2.loli.net/2022/04/22/pgPs2lw7E9YifI6.png)

> **注意**：尽管我们对 Order 和 Customer 保存操作进行了位置的调换，但从其执行过程可以发现：
>
> 单向 1-n 关联关系执行保存时，一定会多出 UPDATE 语句 ==> 因为 n 的一端在插入时不会同时插入外键列

### 2.2、获取

```java
@Test
public void testFind() {
    Customer customer = entityManager.find(Customer.class, 1);
    System.out.println(customer.getLastName());
    System.out.println(customer.getOrders().size());
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
customer1
Hibernate: 
    select
        orders0_.CUSTOMER_ID as CUSTOMER3_0_1_,
        orders0_.id as id1_1_1_,
        orders0_.id as id1_1_0_,
        orders0_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_ORDER orders0_ 
    where
        orders0_.CUSTOMER_ID=?
2
```

> **注意**：默认情况下，对关联的多的一方使用懒加载的加载策略
>
> 这里注意与单向多对一的区别：单向多对一——默认情况下，使用左外连接获取 n 的一端的对象和其关联的 1 的一端的对象

同理，可以通过修改 Customer 实体类中 `@OneToMany` 的 `fetch` 属性值，来改变其默认的加载策略

```java
@JoinColumn(name = "CUSTOMER_ID")
// - fetch 设置默认的加载策略
@OneToMany(fetch = FetchType.EAGER)
public Set<Order> getOrders() {
    return orders;
}
```

再次测试，查看日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_0_1_,
        customer0_.age as age2_0_1_,
        customer0_.birthDay as birthDay3_0_1_,
        customer0_.createTime as createTi4_0_1_,
        customer0_.email as email5_0_1_,
        customer0_.LAST_NAME as LAST_NAM6_0_1_,
        orders1_.CUSTOMER_ID as CUSTOMER3_0_3_,
        orders1_.id as id1_1_3_,
        orders1_.id as id1_1_0_,
        orders1_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    left outer join
        JPA_ORDER orders1_ 
            on customer0_.id=orders1_.CUSTOMER_ID 
    where
        customer0_.id=?
customer1
2
```

可以看到，`fetch` 属性值修改为 `EAGER` 时，JPA 会使用左外连接获取 1 的一端的对象和其关联的 n 的一端的对象（注意这里与单向多对一的区别！！！）

### 2.3、删除

**删除 n 的一端**

```java
@Test
public void testRemoveN() {
    Order order = entityManager.find(Order.class, 5);
    entityManager.remove(order);
}
```

日志信息

```sql
Hibernate: 
    select
        order0_.id as id1_1_0_,
        order0_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_ORDER order0_ 
    where
        order0_.id=?
Hibernate: 
    delete 
    from
        JPA_ORDER 
    where
        id=?
```

**删除 1 的一端**

```java
@Test
public void testRemove1() {
    Customer customer = entityManager.find(Customer.class, 1);
    entityManager.remove(customer);
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_0_1_,
        customer0_.age as age2_0_1_,
        customer0_.birthDay as birthDay3_0_1_,
        customer0_.createTime as createTi4_0_1_,
        customer0_.email as email5_0_1_,
        customer0_.LAST_NAME as LAST_NAM6_0_1_,
        orders1_.CUSTOMER_ID as CUSTOMER3_0_3_,
        orders1_.id as id1_1_3_,
        orders1_.id as id1_1_0_,
        orders1_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    left outer join
        JPA_ORDER orders1_ 
            on customer0_.id=orders1_.CUSTOMER_ID 
    where
        customer0_.id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=null 
    where
        CUSTOMER_ID=?
Hibernate: 
    delete 
    from
        JPA_CUSTOMERS 
    where
        id=?
```

JPA_CUSTOMERS 表数据

![image-20220422001850193](https://s2.loli.net/2022/04/22/omyFiVsMZC1xEBt.png)

JPA_ORDERS 表数据

![image-20220422001839462](https://s2.loli.net/2022/04/22/gU5siuZd1hmNG8O.png)

可以发现，默认情况下，若删除 1 的一端，则会先将有关联的数据外键值置为空后，再去执行 delete 操作

> **注意**：这里注意单向一对多与单向多对一的区别。单向多对一不能直接删除 1 的一端，因为有外键约束；而单向一对多可以删除 1 的一端

Q：那可以对单向一对多删除 1 的一端时，不选择将有关联的数据外键值置为空，而选择将有关联的数据直接删除，允许这样操作吗？

A：答案是肯定的，当然可以！只需要为 `@OneToMany` 指定 `cascade` 属性值即可

```java
@JoinColumn(name = "CUSTOMER_ID")
// - cascade 设置默认的删除策略
@OneToMany(cascade = {CascadeType.REMOVE})
public Set<Order> getOrders() {
    return orders;
}
```

再次测试，查看日志信息进行验证

```sql
Hibernate: 
    select
        customer0_.id as id1_0_1_,
        customer0_.age as age2_0_1_,
        customer0_.birthDay as birthDay3_0_1_,
        customer0_.createTime as createTi4_0_1_,
        customer0_.email as email5_0_1_,
        customer0_.LAST_NAME as LAST_NAM6_0_1_,
        orders1_.CUSTOMER_ID as CUSTOMER3_0_3_,
        orders1_.id as id1_1_3_,
        orders1_.id as id1_1_0_,
        orders1_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    left outer join
        JPA_ORDER orders1_ 
            on customer0_.id=orders1_.CUSTOMER_ID 
    where
        customer0_.id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=null 
    where
        CUSTOMER_ID=?
Hibernate: 
    delete 
    from
        JPA_ORDER 
    where
        id=?
Hibernate: 
    delete 
    from
        JPA_ORDER 
    where
        id=?
Hibernate: 
    delete 
    from
        JPA_CUSTOMERS 
    where
        id=?
```

JPA_CUSTOMERS 表数据

![image-20220422001850193](https://s2.loli.net/2022/04/22/omyFiVsMZC1xEBt.png)

JPA_ORDERS 表数据

![image-20220422003452687](https://s2.loli.net/2022/04/22/l9dPIuGFwO2JDiY.png)

可以发现，当 `@OneToMany` 的 `cascade` 属性值设置为 `Remove` 时，首先会将多的一端的外键列置空。然后会对 JPA_ORDER 表进行数据的删除操作

### 2.4、更新

```java
@Test
public void testUpdate() {
    Customer customer = entityManager.find(Customer.class, 2);
    customer.getOrders().iterator().next().setOrderName("Customer2-Order1-Update");
}
```

日志信息

```sql
Hibernate: 
    select
        customer0_.id as id1_0_1_,
        customer0_.age as age2_0_1_,
        customer0_.birthDay as birthDay3_0_1_,
        customer0_.createTime as createTi4_0_1_,
        customer0_.email as email5_0_1_,
        customer0_.LAST_NAME as LAST_NAM6_0_1_,
        orders1_.CUSTOMER_ID as CUSTOMER3_0_3_,
        orders1_.id as id1_1_3_,
        orders1_.id as id1_1_0_,
        orders1_.ORDER_NAME as ORDER_NA2_1_0_ 
    from
        JPA_CUSTOMERS customer0_ 
    left outer join
        JPA_ORDER orders1_ 
            on customer0_.id=orders1_.CUSTOMER_ID 
    where
        customer0_.id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        ORDER_NAME=? 
    where
        id=?
```

JPA_ORDERS 表数据

![image-20220422003619841](https://s2.loli.net/2022/04/22/9T5bnh8ViZv3YCp.png)



## 3、双向一对多（双向多对一）

*双向一对多* 和 *双向多对一* 本质是一样的。从左侧来看就是 *一对多*，从右侧来看就是 *多对一*

Customer 实体类（一）

```java
@Entity
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;

    private Date birthDay;
    private Date createTime;

    private Set<Order> orders = new HashSet<>();

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "LAST_NAME", length = 50, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
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

    @Temporal(TemporalType.DATE)
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JoinColumn(name = "CUSTOMER_ID")
    @OneToMany
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
```

Order 实体类（多）

```java
@Table(name = "JPA_ORDER")
@Entity
public class Order {
    private Integer id;
    private String orderName;
    private Customer customer;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ORDER_NAME")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
```

### 3.1、保存

**测试方法 1**：先保存 Customer 对象，后保存 Order 对象

```java
@Test
public void testPersist() {
    Customer customer1 = new Customer();
    customer1.setLastName("customer1");
    customer1.setAge(11);
    customer1.setEmail("customer1@qq.com");
    customer1.setCreateTime(new Date());
    customer1.setBirthDay(new Date());

    Order order1 = new Order();
    order1.setOrderName("Customer1-Order1");
    Order order2 = new Order();
    order2.setOrderName("Customer1-Order2");

    // 设置关联关系——一对多
    Set<Order> orders = customer1.getOrders();
    orders.add(order1);
    orders.add(order2);
    // 设置关联关系——多对一
    order1.setCustomer(customer1);
    order2.setCustomer(customer1);

    entityManager.persist(customer1);
    entityManager.persist(order1);
    entityManager.persist(order2);
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
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMERS_ID, ORDER_NAME) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMERS_ID, ORDER_NAME) 
    values
        (?, ?)
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=? 
    where
        id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=? 
    where
        id=?
```

JPA_CUSTOMERS 表数据

![image-20220422214032703](https://s2.loli.net/2022/04/22/RPoyzXndB6JVM8j.png)

JPA_ORDERS 表数据

![image-20220422214010236](https://s2.loli.net/2022/04/22/DcwW7odr6O9BgMJ.png)

> **分析**：由于是先保存 Customer 对象（即一的一方），所以在保存 Order 对象（即多的一方）时，CUSTOMER_ID 已经有了，因此在执行
>
> insert 
>     into
>         JPA_ORDER
>         (CUSTOMERS_ID, ORDER_NAME) 
>     values
>         (?, ?)
>
> 时，CUSTOMER_ID 就已经被插入了；而多出来的
>
> update
>         JPA_ORDER 
>     set
>         CUSTOMER_ID=? 
>     where
>         id=?
>
> 是由 Customer 来维护的

**测试方法 2**：先保存 Order 对象，后保存 Customer 对象

```java
@Test
public void testPersist2() {
    Customer customer2 = new Customer();
    customer2.setLastName("customer2");
    customer2.setAge(22);
    customer2.setEmail("customer2@qq.com");
    customer2.setCreateTime(new Date());
    customer2.setBirthDay(new Date());

    Order order1 = new Order();
    order1.setOrderName("Customer2-Order1");
    Order order2 = new Order();
    order2.setOrderName("Customer2-Order2");

    // 设置关联关系——一对多
    Set<Order> orders = customer2.getOrders();
    orders.add(order1);
    orders.add(order2);
    // 设置关联关系——多对一
    order1.setCustomer(customer2);
    order2.setCustomer(customer2);

    entityManager.persist(order1);
    entityManager.persist(order2);
    entityManager.persist(customer2);
}
```

日志信息

```sql
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMER_ID, ORDER_NAME) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMER_ID, ORDER_NAME) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        JPA_CUSTOMERS
        (age, birthDay, createTime, email, LAST_NAME) 
    values
        (?, ?, ?, ?, ?)
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=?,
        ORDER_NAME=? 
    where
        id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=?,
        ORDER_NAME=? 
    where
        id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=? 
    where
        id=?
Hibernate: 
    update
        JPA_ORDER 
    set
        CUSTOMER_ID=? 
    where
        id=?
```

JPA_CUSTOMERS 表数据

![image-20220422214107200](https://s2.loli.net/2022/04/22/gMlcmFkOx4NnJq2.png)

JPA_ORDERS 表数据

![image-20220422214120003](https://s2.loli.net/2022/04/22/KvlNQwTE4ZHnXpr.png)

> **分析**：由于是先保存 Order 对象（即多的一方），此时 CUSTOMER_ID 还没有值，因此在执行
>
> insert 
>     into
>         JPA_ORDER
>         (CUSTOMERS_ID, ORDER_NAME) 
>     values
>         (?, ?)
>
> 时，CUSTOMER_ID 为空；而多出来的 update 语句，即
>
> update
>         JPA_ORDER 
>     set
>         CUSTOMER_ID=?,
>         ORDER_NAME=? 
>     where
>         id=?
>
> 是由 Order 来维护的，
>
> update
>         JPA_ORDER 
>     set
>         CUSTOMER_ID=? 
>     where
>         id=?
>
> 是有 Customer 来维护的

由上可以总结

> **总结**：双向一对多（双向多对一）执行保存时
>
> 1）先保存 1 的一方，默认会多出 m 条 update 语句（m 为 n 的一方个数）
>
> 2）先保存 n 的一方，默认会多出 2m 条 update 语句（m 为 n 的一方个数）
>
> **建议**：当关联关系为双向 1-n 时，建议使用 n 的一方来维护关联关系，1 的一方不维护关联关系，这样可以有效减少 SQL 语句

**Q**：那么如何让 1 的一方不维护关联关系呢？

**A**：可以通过在 1 的一方的 `@OneToMany` 注解中使用 `mappedBy` 属性来实现

```java
@Entity
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    // ...
    @JoinColumn(name = "CUSTOMER_ID")
    @OneToMany(mappedBy = "customer")
    public Set<Order> getOrders() {
        return orders;
    }
    // ...
}
```

属性值指定为 n 的一方中 1 的一方属性名，什么意思呢？

```java
@Table(name = "JPA_ORDER")
@Entity
public class Order {
    // ...
    private Customer customer; // 该属性名就是 mappedBy 属性值
    // ...
}
```

现在直接来执行**测试方法 1**，观察日志信息有何问题

```console
javax.persistence.PersistenceException: [PersistenceUnit: jpa-1] Unable to build EntityManagerFactory
<3 internal lines>
at javax.persistence.Persistence.createEntityManagerFactory(Persistence.java:63)
	at javax.persistence.Persistence.createEntityManagerFactory(Persistence.java:47)
	at helloworld.bilateral.One2ManyTest.init(One2ManyTest.java:23) <27 internal lines>
Caused by: org.hibernate.AnnotationException: Associations marked as mappedBy must not define database mappings like @JoinTable or @JoinColumn: com.vectorx.jpa.helloworld.bilateral.one2many.Customer.orders <10 internal lines>
	... 32 more
```

这里，报错信息告诉我们，使用 `mappedBy` 时，不能在 Customer 中使用 `@JoinColumn` 注解了。既然如此，那就注释掉呗

```java
@Entity
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    // ...
    //@JoinColumn(name = "CUSTOMER_ID")
    @OneToMany(mappedBy = "customer")
    public Set<Order> getOrders() {
        return orders;
    }
    // ...
}
```

再来执行**测试方法 1**，观察日志信息是否能正常执行

```console
Hibernate: 
    insert 
    into
        JPA_CUSTOMERS
        (age, birthDay, createTime, email, LAST_NAME) 
    values
        (?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMER_ID, ORDER_NAME) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        JPA_ORDER
        (CUSTOMER_ID, ORDER_NAME) 
    values
        (?, ?)
```

可以发现，执行没有报错，而且也不会多出 update 语句

JPA_CUSTOMERS 表数据

![image-20220422222317140](https://s2.loli.net/2022/04/22/i73nG5bJHFKBdzo.png)

JPA_ORDERS 表数据

![image-20220422222329839](https://s2.loli.net/2022/04/22/UZ51T6k3HVWCsBJ.png)

### 3.2、获取、删除、更新

这里的操作都能在 *单向多对一* 和 *单向一对多* 中找到对应的实现，这里就不一一细说了

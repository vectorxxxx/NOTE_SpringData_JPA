> 笔记来源：[尚硅谷jpa开发教程全套完整版(初学者零基础入门)](https://www.bilibili.com/video/BV1vW411M7zp)

[TOC]

# JPA 注解

## 基本注解

JPA 基本注解有 6 个：

- `@Entity`
- `@Table`
- `@Id`
- `@GeneratedValue`
- `@Column`
- `@Basic`

除此之外，还有 `@Transient`、`@Temporal` 等注解



## 1、@Entity

`@Entity` 标注用于实体类声明语句之前，<mark>指出该 Java 类为实体类，将映射到指定的数据库表</mark>

如声明一个实体类 Customer，它将映射到数据库中的 customer 表上

```java
@Entity
public class Customer
```

测试结果

```console
Hibernate: 
    insert 
    into
        Customer
        (age, email, LAST_NAME) 
    values
        (?, ?, ?)
```

可以发现，加了 `@Entity` 注解之后，Customer 变成了一个实体类，具备了与数据表的映射关系；只不过表名和类名是一样的



## 2、@Table

当<mark>实体类与其映射的数据库表不同名</mark>时，需要使用 `@Table` 标注说明。该标注与 `@Entity` 标注并列使用，置于实体类声明语句之前，可写于单独语句行，也可与声明语句同行

`@Table` 标注的常用选项是 `name`，用于指明数据库的表名

`@Table` 标注还有几个选项：

- `catalog` 和 `schema`，用于设置表所属的数据库目录或模式，通常为数据库名
- `uniqueConstraints` 选项用于设置约束条件，通常不须设置

```java
@Table(name = "JPA_CUSTOMERS")
@Entity
public class Customer
```

测试结果

```console
Hibernate: 
    insert 
    into
        JPA_CUSTOMERS
        (age, email, LAST_NAME) 
    values
        (?, ?, ?)
```



## 3、@Id

`@Id` 标注用于声明一个实体类的属性映射为数据库的<mark>主键列</mark>。该属性通常置于属性声明语句之前，可与声明语句同行，也可写在单独行上

`@Id` 标注也可置于属性的 Getter 方法之前

```java
@Id
public Integer getId() {
    return id;
}
```



## 4、@GeneratedValue 

`@GeneratedValue` 用于标注<mark>主键的生成策略</mark>，通过 `strategy ` 属性指定

默认情况下，JPA 自动选择一个最适合底层数据库的主键生成策略：

- SQLServer 对应 identity
- MySQL 对应 auto increment

在 `javax.persistence.GenerationType` 中定义了以下几种可供选择的策略：

- `IDENTITY`：采用数据库 ID 自增长的方式来自增主键字段，Oracle 不支持这种方式
- `AUTO`：<mark>JPA 自动选择合适的策略，是默认选项</mark>
- `SEQUENCE`：通过序列产生主键，通过 `@SequenceGenerator` 注解指定序列名，MySQL 不支持这种方式
- `TABLE`：通过表产生主键，框架借由表模拟序列产生主键，使用该策略可以使应用更易于数据库移植

```java
@GeneratedValue(strategy = GenerationType.AUTO)
@Id
public Integer getId() {
    return id;
}
```



## 5、@Basic

`@Basic` 表示一个简单属性到数据库表字段的映射，对于<mark>没有任何标注</mark>的 getXxx() 方法，默认即为 `@Basic` 

- `fetch`：表示该属性的读取策略，有 `EAGER` 和 `LAZY` 两种，分别表示主支抓取和延迟加载，默认为 `EAGER`
- `optional`：表示该属性是否允许为 null，默认为 true

```java
@Basic
public String getEmail() {
    return email;
}
```



## 6、@Column

当<mark>实体的属性与其映射的数据库表的列不同名</mark>时需要使用，`@Column` 标注通常置于实体的属性声明语句之前，还可与 `@Id` 标注一起使用

`@Column` 标注的常用属性是 `name`，用于设置映射数据库表的列名。此外，该标注还包含其它多个属性，如：`unique`、`nullable`、`length`等

`@Column` 标注的 `columnDefinition` 属性表示<mark>该字段在数据库中的实际类型</mark>

- 通常 ORM 框架可以根据属性类型自动判断数据库中字段的类型，但是对于 Date 类型仍无法确定数据库中字段类型究竟是 `DATE`、`TIME` 还是 `TIMESTAMP`
- 此外，String 的默认映射类型为 `VARCHAR`，如果要将 String 类型映射到特定数据库的 `BLOB` 或 `TEXT` 字段类型

`@Column` 标注也可置于属性的 Getter 方法之前

```java
@Column(name = "LAST_NAME", length = 50, nullable = false)
public String getLastName() {
    return lastName;
}
```



## 7、@Transient

表示<mark>该属性并非是一个到数据库表字段的映射</mark>，ORM 框架将忽略该属性

如果一个属性并非数据库表的字段映射，就务必将其标示为 `@Transient`，否则 ORM 框架默认其注解为 `@Basic`

```java
// 工具方法，不需要映射为数据表的一列
@Transient
public String getInfo() {
    return "lastName: " + this.lastName + ", email: " + email;
}
```



## 8、@Temporal

在核心的 Java API 中并没有定义 Date 类型的精度（temporal precision）

而在数据库中，表示 Date 类型的数据有 `DATE`、`TIME` 和 `TIMESTAMP` 三种精度（即单纯的日期、时间或者两者兼备）

在进行属性映射时可使用 `@Temporal` 注解来调整精度

```java
@Temporal(TemporalType.DATE)
public Date getBirthDay() {
    return birthDay;
}
@Temporal(TemporalType.TIMESTAMP)
public Date getCreateTime() {
    return createTime;
}
```

测试结果

```console
Hibernate: 
    insert 
    into
        JPA_CUSTOMERS
        (age, birthDay, createTime, email, LAST_NAME) 
    values
        (?, ?, ?, ?, ?)
```

查看数据表结构

![image-20220409162824938](https://s2.loli.net/2022/04/09/5lZtcsNkfb1ASxP.png)

查询数据表数据

![image-20220409162724661](https://s2.loli.net/2022/04/09/Sd58NqCmlOGofHc.png)



## 9、用 table 生成主键详解

将当前主键的值单独保存到一个数据库的表中，主键的值每次都是从指定的表中查询来获得

这种方法生成主键的策略可以适用于任何数据库，不必担心不同数据库不兼容造成的问题

1）准备工作

```SQL
create table JPA_ID_GENERATOR
(
	ID int(10) auto_increment,
	PK_NAME varchar(50) not null,
	PK_VALUE int(10) not null,
	constraint JPA_ID_GENERATOR_pk
		primary key (ID)
);
INSERT INTO jpa.jpa_id_generator (ID, PK_NAME, PK_VALUE) VALUES (1, 'CUSTOMER_ID', 1);
INSERT INTO jpa.jpa_id_generator (ID, PK_NAME, PK_VALUE) VALUES (2, 'STUDENT_ID', 10);
INSERT INTO jpa.jpa_id_generator (ID, PK_NAME, PK_VALUE) VALUES (3, 'ORDER_ID', 100);
```

表结构

![image-20220409164358343](https://s2.loli.net/2022/04/09/HwE8XBtkp9hFMNO.png)

表数据

![image-20220409170429221](https://s2.loli.net/2022/04/09/CQWSlgi43NGcEjO.png)

2）编写注解，`@TableGenerator` 和 `@GeneratedValue` 配合使用

```java
@TableGenerator(
    name = "ID_GENERATOR",
    table = "JPA_ID_GENERATOR",
    pkColumnName = "PK_NAME",
    pkColumnValue = "CUSTOMER_ID",
    valueColumnName = "PK_VALUE",
    allocationSize = 100
)
@GeneratedValue(strategy = GenerationType.TABLE, generator = "ID_GENERATOR")
@Id
public Integer getId() {
    return id;
}
```

其中 `pkColumnName`、`pkColumnValue` 和 `valueColumnName` 之间的关系如下图所示

![image-20220409164930953](https://s2.loli.net/2022/04/09/SmgTpuhPF2QVCId.png)

`@TableGenerator` 注解中各个属性详解：

- `name`：主键生成策略名，与 `@GeneratedValue` 注解中 `generator` 值对应
- `table`：表生成策略所持久化的表名
- `pkColumnName`：在持久化表中，主键生成策略所对应的键
- `pkColumnValue`：在持久化表中，主键生成策略所对应的值
- `valueColumnName`：在持久化表中，主键当前所生成的值
- `allocationSize`：每次主键增加的大小

3）测试结果

后台日志信息

```

Hibernate: 
    select
        PK_VALUE 
    from
        JPA_ID_GENERATOR 
    where
        PK_NAME = 'CUSTOMER_ID' for update
            
Hibernate: 
    update
        JPA_ID_GENERATOR 
    set
        PK_VALUE = ? 
    where
        PK_VALUE = ? 
        and PK_NAME = 'CUSTOMER_ID'
Hibernate: 
    insert 
    into
        JPA_CUSTOMERS
        (age, birthDay, createTime, email, LAST_NAME, id) 
    values
        (?, ?, ?, ?, ?, ?)
```

JPA_ID_GENERATOR 表数据

![image-20220409171039021](https://s2.loli.net/2022/04/09/csIaCqYDLloQPxk.png)

JPA_CUSTOMERS 表数据

![image-20220409171059216](https://s2.loli.net/2022/04/09/9Let2qlzpyE4DA3.png)



## 总结

本节重点掌握：

- 类注解 `@Entity`、`@Table` 的作用
- 主键注解 `Id`、`@GeneratedValue` 的作用
- 属性注解 `@Column` 的作用
- `@Basic` 和 `@Transient` 注解的区别
- `@Temporal` 注解的使用
- `@TableGenerator` 注解的使用

附上导图，仅供参考

![01-JPA 注解](https://s2.loli.net/2022/04/09/9JDeSaP4TQtVbik.png)


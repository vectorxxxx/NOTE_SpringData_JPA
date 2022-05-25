> 笔记来源：[尚硅谷SpringData教程(springdata经典，spring data快速上手)](https://www.bilibili.com/video/BV1hW411g7jy)

[TOC]

# @Query 与 @Modifying

## 1、@Query 注解

### 1.1、自定义查询

这种查询可以声明在 Repository 方法中，<mark>摆脱像命名查询那样的约束，将查询直接在相应的接口方法中声明</mark>，结构更为清晰，这是 Spring data 的特
有实现

```java
@Query("SELECT C FROM Customer c WHERE c.customerId=?1")
Customer testGetByCustomerId2(Integer id);
```

**测试方法**

PersonRepository 类

```java
@Query("select p from Person p where p.id=(select max(id) from Person p)")
Person getMaxIdPerson();
```

Test 类

```java
@Test
public void testGetMaxIdPerson() {
    Person person = repository.getMaxIdPerson();
    System.out.println(person);
}
```

日志信息

```sql
Hibernate: 
    select
        person0_.id as id1_1_,
        person0_.add_id as add_id6_1_,
        person0_.address_id as address_2_1_,
        person0_.birth_day as birth_da3_1_,
        person0_.email as email4_1_,
        person0_.last_name as last_nam5_1_ 
    from
        jpa_persons person0_ 
    where
        person0_.id=(
            select
                max(person1_.id) 
            from
                jpa_persons person1_
        )
Person{id=27, lastName='XY', email='XY@qq.com', birthDay=2022-05-21 22:48:34.0}
```

> **结论**：使用 `@Query` 注解可以自定义 JPQL 语句以实现更灵活的查询

### 1.2、索引参数与命名参数

#### 索引参数

索引参数如下所示，索引值从 1 开始，查询中 “?X” 个数需要与方法定义的参数个数相一致，并且顺序也要一致

```java
@Modifying
@Query（"update User u set u.firstname=?1 where u.lastname=?2"）
int setFixedFirstnameFor(String firstname，String lastname);
```

#### 命名参数

命名参数（推荐使用这种方式）：可以定义好参数名，赋值时采用 `@Param("参数名")`，而不用管顺序

```java
@Query（"select cirom customer u where c.firstname=：firstname or c.lastname=：lastname"）
Customer findByLastnameOrFirstname(@Param("lastname") String lastname, @Param("firstname") String firstname);
```

**测试方法 1**

PersonRepository 类

```java
@Query("select p from Person p where p.lastName=?1 and p.email=?2")
Person getByLastNameAndEmail(String lastName, String email);
```

Test 类

```java
@Test
public void testGetByLastNameAndEmail() {
    Person person = repository.getByLastNameAndEmail("AA", "AA@qq.com");
    System.out.println(person);
}
```

日志信息

```sql
Hibernate: 
    select
        person0_.id as id1_1_,
        person0_.add_id as add_id6_1_,
        person0_.address_id as address_2_1_,
        person0_.birth_day as birth_da3_1_,
        person0_.email as email4_1_,
        person0_.last_name as last_nam5_1_ 
    from
        jpa_persons person0_ 
    where
        person0_.last_name=? 
        and person0_.email=?
Person{id=1, lastName='AA', email='AA@qq.com', birthDay=2022-05-21 22:48:34.0}
```

**测试方法 2**

PersonRepository 类

```java
@Query("select p from Person p where p.lastName=:lastName and p.email=:email")
Person getByEmailAndLastName(@Param("email") String email, @Param("lastName") String lastName);
```

Test 类

```java
@Test
public void testGetByEmailAndLastName() {
    Person person = repository.getByEmailAndLastName("BB@qq.com", "BB");
    System.out.println(person);
}
```

日志信息

```sql
Hibernate: 
    select
        person0_.id as id1_1_,
        person0_.add_id as add_id6_1_,
        person0_.address_id as address_2_1_,
        person0_.birth_day as birth_da3_1_,
        person0_.email as email4_1_,
        person0_.last_name as last_nam5_1_ 
    from
        jpa_persons person0_ 
    where
        person0_.last_name=? 
        and person0_.email=?
Person{id=2, lastName='BB', email='BB@qq.com', birthDay=2022-05-21 22:48:34.0}
```

### 1.3、模糊查询

如果是 `@Query` 中有 `LIKE` 关键字，后面的参数需要前面或者后面加 `%`，这样在传递参数值的时候就可以不加 `%`：

#### 右模糊

```java
@Query("select o from UserModel o where o.name like ?1%"）
public List<UserModel> findByUuidOrAge(String name);
```

#### 左模糊

```java
@Query("select o from UserModel o where o.name like %?1"）
public List<UserModel> findByUuidOrAge(String name);
```

#### 全模糊

```java
@Query("select o from UserModel o where o.name like %?1%"）
public List<UserModel> findByUuidOrAge(String name);
```

**测试方法 1**

PersonRepository 类

```java
@Query("select p from Person p where p.lastName like %?1% or p.email like %?2%")
List<Person> findLikeLastNameOrEmail(String lastName, String email);
```

Test 类

```java
@Test
public void testFindLikeLastNameOrEmail() {
    List<Person> personList = repository.findLikeLastNameOrEmail("A", "BB");
    System.out.println(personList);
}
```

日志信息

```sql
Hibernate: 
    select
        person0_.id as id1_1_,
        person0_.add_id as add_id6_1_,
        person0_.address_id as address_2_1_,
        person0_.birth_day as birth_da3_1_,
        person0_.email as email4_1_,
        person0_.last_name as last_nam5_1_ 
    from
        jpa_persons person0_ 
    where
        person0_.last_name like ? 
        or person0_.email like ?
[Person{id=1, lastName='AA', email='AA@qq.com', birthDay=2022-05-21 22:48:34.0}, Person{id=2, lastName='BB', email='BB@qq.com', birthDay=2022-05-21 22:48:34.0}]
```

**测试方法 2**

PersonRepository 类

```java
@Query("select p from Person p where p.lastName like %:lastName% or p.email like %:email%")
List<Person> findLikeEmailOrLastName(@Param("email") String email, @Param("lastName") String lastName);
```

Test 类

```java
@Test
public void testFindLikeEmailOrLastName() {
    List<Person> personList = repository.findLikeEmailOrLastName("DD", "C");
    System.out.println(personList);
}
```

日志信息

```sql
Hibernate: 
    select
        person0_.id as id1_1_,
        person0_.add_id as add_id6_1_,
        person0_.address_id as address_2_1_,
        person0_.birth_day as birth_da3_1_,
        person0_.email as email4_1_,
        person0_.last_name as last_nam5_1_ 
    from
        jpa_persons person0_ 
    where
        person0_.last_name like ? 
        or person0_.email like ?
[Person{id=3, lastName='CC', email='CC@qq.com', birthDay=2022-05-21 22:48:34.0}, Person{id=4, lastName='DD', email='DD@qq.com', birthDay=2022-05-21 22:48:34.0}]
```

### 1.4、本地查询

还可以使用 `@Query` 来指定本地查询，只要设置 `nativeQuery` 为 `true`，比如：

```java
@Query(value="select * from tbl_user where name like %?1", nativeQuery=true)
public List<UserModel> findByUuidOrAge(String name);
```

**测试方法**

PersonRepository 类

```java
@Query(value = "select count(1) FROM jpa_persons", nativeQuery = true)
int getTotalCount();
```

Test 类

```java
@Test
public void testGetTotalCount() {
    int totalCount = repository.getTotalCount();
    System.out.println(totalCount);
}
```

日志信息

```sql
Hibernate: 
    select
        count(1) 
    FROM
        jpa_persons
27
```



## 2、@Modifying 注解

### 2.1、执行更新操作

`@Query `与 `@Modifying` 这两个 `annotation` 一起声明，可定义个性化更新操作，例如只涉及某些字段更新时最为常用，示例如下：

```java
@Modifying
@Query("UPDATE Customerg SET c.customerName=?1")
int updateCustomerName(String cn);
```

注意：

- 方法的返回值应该是 `int`，表示更新语句所影响的行数
- 在调用的地方必须加事务，没有事务不能正常执行

**测试方法 1**

> 不加 `@Modifying` 注解会怎么样？

PersonRepository 类

```java
@Query("update Person p set p.email=:email where p.id=:id")
int updateEmailById(@Param("email") String email, @Param("id") Integer id);
```

Test 类

```java
@Test
public void testUpdateEmailById() {
    int updateCount = repository.updateEmailById("aaaa@qq.com", 1);
    System.out.println(updateCount);
}
```

日志信息

```java
org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.vectorx.springdata.entities.Person p set p.email=:email where p.id=:id]; nested exception is java.lang.IllegalStateException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.vectorx.springdata.entities.Person p set p.email=:email where p.id=:id]
	//...
Caused by: java.lang.IllegalStateException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.vectorx.springdata.entities.Person p set p.email=:email where p.id=:id]
	//...
Caused by: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.vectorx.springdata.entities.Person p set p.email=:email where p.id=:id]
```

报错抛出了 `QueryExecutionRequestException` 异常，提示 Query 不支持 DML 更新操作

**测试方法 2**

> 仅仅加 `@Modifying` 注解会怎么样？

PersonRepository 类：唯一区别就是加上 `@Modifying` 注解

```java
@Modifying
@Query("update Person p set p.email=:email where p.id=:id")
int updateEmailById(@Param("email") String email, @Param("id") Integer id);
```

日志信息

```java
org.springframework.dao.InvalidDataAccessApiUsageException: Executing an update/delete query; nested exception is javax.persistence.TransactionRequiredException: Executing an update/delete query
	//...
Caused by: javax.persistence.TransactionRequiredException: Executing an update/delete query
	//...
```

报错抛出了 `TransactionRequiredException` 异常，表明更新或删除操作必须要有一个事务

### 2.2、事务

Spring Data 提供了默认的事务处理方式，即所有的查询均声明为 *只读事务*

对于自定义的方法，如需改变 Spring Data 提供的事务默认方式，可以在方法上注解 `@Transactional` 声明

进行多个 Repository 操作时，也应该使它们在同一个事务中处理，按照分层架构的思想，这部分属于业务逻辑层。因此，需要在 Service 层实现对多个 Repository 的调用，并在相应的方法上声明事务

**测试方法 3**

> `@Modifying` 和 `@Transactional` 注解都加上

PersonService 类

```java
@Service
public class PersonService
{
    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public int updateEmailById(String email, Integer id) {
        return personRepository.updateEmailById(email, id);
    }
}
```

Test 类

```java
public class SpringDataTest
{
    private ApplicationContext context;
    private PersonRepository repository;
    private PersonService service;
    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        repository = context.getBean(PersonRepository.class);
        service = context.getBean(PersonService.class);
    }

    // ...
    
    @Test
    public void testUpdateEmailById() {
        int updateCount = service.updateEmailById("aaaa@qq.com", 1);
        System.out.println(updateCount);
    }
}
```

日志信息

```java
Hibernate: 
    update
        jpa_persons 
    set
        email=? 
    where
        id=?
1
```

数据表 JPA_PERSONS 数据

![image-20220525214542067](https://s2.loli.net/2022/05/25/QKZEGBwDIiCmb6n.png)

> **结论**：
>
> 1、可以通过自定义的 JPQL 完成 UPDATE 和 DELETE 操作。注意：JPQL 不支持使用 INSERT
>
> 2、在 `@Query` 注解中编写 JPQL 语句，但必须使用 `@Modifying` 进行修饰。以通知 SpringData 这是一个 UPDATE 或 DELETE 操作
>
> 3、UPDATE 或 DELETE 操作需要使用事务，此时需要定义 Service 层。在 Service 层的方法上添加事务操作
>
> 4、默认情况下，SpringData 的每个方法上有事务，但都是一个只读事务。他们不能完成修改操作！



## 总结

本节主要掌握 `@Query` 和 `@Modifying` 两个注解：

- `@Query` 注解支持索引参数、命名参数、模糊查询和本地查询
- `@Modifying` 注解支持更新操作，如 update 和 delete，但不支持 insert（准确来说，JPQL 不支持 insert）；需要结合 `@Query` 和 `@Transactional` 注解使用

附上导图，仅供参考

![@Query 与 @Modifying](https://s2.loli.net/2022/05/25/T7rvyU5zqJBetab.png)

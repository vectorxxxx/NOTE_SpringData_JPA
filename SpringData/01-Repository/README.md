> 笔记来源：[尚硅谷SpringData教程(springdata经典，spring data快速上手)](https://www.bilibili.com/video/BV1hW411g7jy)

[TOC]

# Repository

## 1、Repository 接口

Repository 接口是 Spring Data 的一个核心接口，它不提供任何方法（空接口/标记接口），开发者需要在自己定义的接口中声明需要的方法

```java
public interface Repository<T，ID extends Serializable>{}
```

Spring Data 可以让我们只定义接口，只要遵循 Spring Data 的规范，就无需写实现类

> 若我们定义的接口继承了 Repository，则该接口会被 IOC 容器识别为一个 Repository Bean 纳入到 IOC 容器中，进而可以在该接口中定义满足一定规范的方法

```java
public interface PersonRepository extends Repository<Person, Integer>
{
    Person getByLastName(String lastName);
}
```

与继承 Repository 等价的一种方式，就是在持久层接口上使用 `@RepositoryDefinition` 注解，并为其指定 `domainClass` 和 `idClass` 属性

```java
@RepositoryDefinition(domainClass = Person.class, idClass = Integer.class)
public interface PersonRepository
{
    Person getByLastName(String lastName);
}
```



## 2、Repository 子接口

基础的 Repository 提供了最基本的数据访问功能，其几个子接口则扩展了一些功能。它们的继承关系如下：

- `Repository`：仅仅是一个标识，表明任何继承它的均为 *仓库接口类*
- `CrudRepository`：继承 Repository，实现了一组 CRUD 相关的方法
- `PagingAndSortingRepository`：继承 CrudRepository，实现了一组分页排序相关的方法
- `JpaRepository`：继承 PagingAndSortingRepository，实现一组 JPA 规范相关的方法
- `自定义的 XxxRepository`：需要继承 JpaRepository，这样的 `XxxRepository` 接口就具备了通用的数据访问控制层的能力
- `JpaSpecificationExecutor`：不属于 Repository 体系，实现一组 JPA Criteria 查询相关的方法

```java
Repository (org.springframework.data.repository)
----CrudRepository (org.springframework.data.repository)
--------PagingAndSortingRepository (org.springframework.data.repository)
------------JpaRepository (org.springframework.data.jpa.repository)
----------------SimpleJpaRepository (org.springframework.data.jpa.repository.support)
--------------------QueryDslJpaRepository (org.springframework.data.jpa.repository.support)
```



## 3、查询方法定义规范

在 Repository 子接口中声明方法不是随便声明的，而需要符合一定的规范

### 3.1、简单条件查询

> 简单条件查询：查询某一个实体类或者集合

按照 Spring Data 的规范，查询方法以 `find | read | get` 开头，涉及条件查询时，条件的属性用条件关键字连接，要注意的是：<mark>条件属性以首字母大写</mark>

**例如**：定义一个 Entity 实体类

```java
class User{
    private String firstName;
    private String lastName;
}
```

使用 And 条件连接时，应这样写：

```java
findByLastNameAndFirstName(String lastName, String firstName);
```

条件的属性名称与个数要与参数的位置与个数一对应

### 3.2、支持的关键字

直接在接口中定义查询方法，如果是符合规范的，可以不用写实现。目前支持的关键字写法如下：

| Keyword                    | Sample                                | JPQL snippet                                                 |
| :------------------------- | :------------------------------------ | :----------------------------------------------------------- |
| **And**                    | `findByLastnameAndFirstname`          | `... where x.lastname=?1 and x.firstname=?2`                 |
| **Or**                     | `findByLastnameOrFirstname`           | `... where x.lastname=?1 or x.firstname=?2`                  |
| **Between**                | `findByStartDateBetween`              | `... where x.startDate between 1? and ?2`                    |
| **LessThan**               | `findByAgeLessThan`                   | `... where x.age<?1`                                         |
| **GreaterThan**            | `findByAgeGreaterThan`                | `... where x.age>?1`                                         |
| **After**                  | `findByStartDateAfter`                | `... where x.startDate>?1`                                   |
| **Before**                 | `findByStartDateBefore`               | `... where x.startDate<?1`                                   |
| **IsNull**                 | `findByAgelsNull`                     | `... where x.age is null`                                    |
| **IsNotNull**, **NotNull** | `findByAge(Is)NotNull`                | `... where x.age not null`                                   |
| **Like**                   | `findByFirstnameLike`                 | `... where x.firstname like ?1`                              |
| **Notlike**                | `findByFirstnameNotlike`              | `... where x.firstname not like ?1`                          |
| **StartingWith**           | `findByFirstnameStartingWith`         | `... where x.firstname like ?1` (parameter bound with appended %) |
| **Endingwith**             | `findByFirstnameEndingWith`           | `... where x.firstname like ?1` (parameter bound with prepended %) |
| **Containing**             | `findByFirstnameContaining`           | `... where x.firstname like ?1` (parameter bound wrapped in %) |
| **OrderBy**                | `findByAgeOrderByLastnameDesc`        | `... where x.age=?1 order by x.lastname desc`                |
| **Not**                    | `findByLastnameNot`                   | `... where x.lastname<>?1`                                   |
| **In**                     | `findByAgeIn(Collection<Age> ages)`   | `... where x.age in ?1`                                      |
| **Notin**                  | `findByAgeNotin(Colection<Age> ages)` | `... where x.age not in ?1`                                  |
| **TRUE**                   | `findByActiveTrue()`                  | `... where x.active=true`                                    |
| **FALSE**                  | `findByActiveFalse()`                 | `... where x.active=false`                                   |

**测试方法 1**

PersonRepository 类

```java
List<Person> findByLastNameStartingWithAndIdLessThan(String lastname, Integer id);
```

Test 类

```java
@Test
public void testFindByLastNameEndingWithAndIdLessThan() {
    List<Person> personList = repository.findByLastNameStartingWithAndIdLessThan("X", 30);
    System.out.println(personList);
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
        (
            person0_.last_name like ?
        ) 
        and person0_.id<?
[Person{id=24, lastName='XX', email='XX@qq.com', birthDay=2022-05-21 22:48:34.0}, Person{id=27, lastName='XY', email='XY@qq.com', birthDay=2022-05-21 22:48:34.0}]
```

**测试方法 2**

PersonRepository 类

```java
List<Person> findByEmailInAndBirthDayLessThan(List<String> emailList, Date birthDay);
```

Test 类

```java
@Test
public void testFindByEmailInAndBirthDayBetween() {
    List<String> emailList = Arrays.asList("AA@qq.com", "CC@qq.com");
    List<Person> personList = repository.findByEmailInAndBirthDayLessThan(emailList, new Date());
    System.out.println(personList);
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
        (
            person0_.email in (
                ? , ?
            )
        ) 
        and person0_.birth_day<?
[Person{id=1, lastName='AA', email='AA@qq.com', birthDay=2022-05-21 22:48:34.0}, Person{id=3, lastName='CC', email='CC@qq.com', birthDay=2022-05-21 22:48:34.0}]
```



## 4、查询方法解析流程

假如创建如下的查询：`findByUserDepUuid()`，框架在解析该方法时，首先剔除 `findBy`，然后对剩下的属性进行解析，假设查询实体为 Doc

- **步骤 1**：先判断 `userDepUuid`（根据 POJO 规范，首字母变为小写）是否为查询实体的一个属性
  - 如果是，则表示根据该属性进行查询
  - 如果没有该属性，继续第二步；
- **步骤 2**：从右往左截取第一个大写字母开头的字符串（此处为 `Uuid`），然后检查剩下的字符串是否为查询实体的一个属性
  - 如果是，则表示根据该属性进行查询
  - 如果没有该属性，则重复第二步，继续从右往左截取；最后假设 `user` 为查询实体的一个属性
- **步骤 3**：接着处理剩下部分（`DepUuid`），先判断 `user` 所对应的类型是否有 `depUuid` 属性
  - 如果有，则表示该方法最终是根据 `Doc.user.depUuid` 的取值进行查询
  - 否则继续按照步骤 2 的规则从右往左截取，最终表示根据 `Doc.user.dep.uuid` 的值进行查询。
- **步骤 4**：可能会存在一种特殊情况，比如 Doc 包含一个 `user` 的属性，也有一个 `userDep` 属性，此时会存在混淆。可以明确在属性之间加上 `_` 以显式表达意图，比如 `findByUser_DepUuid()` 或者 `findByUserDep_uuid()`

特殊的参数：还可以直接在方法的参数上加入分页或排序的参数，比如：

```java
Page<UserModel> findByName(String name, Pageable pageable);
List<UserModel> findByName(String name, Sort sor);
```

**测试方法 1**

Person 类

```java
@Table(name = "JPA_PERSONS")
@Entity
public class Person
{
    // ...
    private Address address;

    @JoinColumn(name = "ADD_ID")
    @ManyToOne
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
```

PersonRepository 类

```java
 List<Person> findByAddressId(Integer addressId);
```

Test 类

```java
@Test
public void testFindByAddressId() {
    List<Person> personList = repository.findByAddressId(123);
    System.out.println(personList);
}
```

日志信息

```sql
Hibernate: 
    select
        person0_.id as id1_1_,
        person0_.address_id as address_5_1_,
        person0_.birth_day as birth_da2_1_,
        person0_.email as email3_1_,
        person0_.last_name as last_nam4_1_ 
    from
        jpa_persons person0_ 
    left outer join
        jpa_addesss address1_ 
            on person0_.address_id=address1_.id 
    where
        address1_.id=?
[]
```

修改 Person 类

```java
@Table(name = "JPA_PERSONS")
@Entity
public class Person
{
    // ...
    private Address address;
    private Integer addressId;
    
    @JoinColumn(name = "ADD_ID")
    @ManyToOne
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}
```

再次测试，查看日志信息

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
        person0_.address_id=?
[]
```

> 查询方法支持属性的级联查询。若当前类有符合条件的属性，则优先使用类中的属性，而不使用级联属性
>
> 若需要使用级联属性，则属性之间使用进行 `_`（下划线）连接

**测试方法 2**

PersonRepository 类

```java
List<Person> findByAddress_Id(Integer addressId);
```

Test 类

```java
@Test
public void testFindByAddress_Id() {
    List<Person> personList = repository.findByAddress_Id(123);
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
    left outer join
        jpa_addesss address1_ 
            on person0_.add_id=address1_.id 
    where
        address1_.id=?
[]
```

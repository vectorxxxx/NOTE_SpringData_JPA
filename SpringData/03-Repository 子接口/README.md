> 笔记来源：[尚硅谷SpringData教程(springdata经典，spring data快速上手)](https://www.bilibili.com/video/BV1hW411g7jy)

[TOC]

# Repository 子接口

## 1、Repository 子接口

在之前 *01-Repository* 中我们介绍过 Repository 的几个子接口，现在重新回顾下这几个子接口的知识

- `CrudRepository`：继承于 Repository，实现了一组 CRUD 相关的方法
- `PagingAndSortingRepository`：继承于 CrudRepository，实现了一组分页排序相关的方法
- `JpaRepository`：继承于 PagingAndSortingRepository，实现了一组 JPA 规范相关的方法

```java
Repository (org.springframework.data.repository)
----CrudRepository (org.springframework.data.repository)
--------PagingAndSortingRepository (org.springframework.data.repository)
------------JpaRepository (org.springframework.data.jpa.repository)
```

另外，还介绍了

- `自定义的 XxxRepository`：需要继承 JpaRepository，这样的 `XxxRepository` 接口就具备了通用的数据访问控制层的能力
- `JpaSpecificationExecutor`：不属于 Repository 体系，实现一组 JPA Criteria 查询相关的方法



## 2、CrudRepository 接口

`CrudRepository` 方法一览

![image-20220526223923967](https://s2.loli.net/2022/05/26/OdYzilcBfZkIS3a.png)

`CrudRepository` 源码解析

```java
@NoRepositoryBean
public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> {
    // 保存实体
	<S extends T> S save(S entity);
    // 保存实体集
	<S extends T> Iterable<S> save(Iterable<S> entities);
    // 通过 id 查找实体
	T findOne(ID id);
    // 判断指定 id 的实体是否存在
	boolean exists(ID id);
    // 返回该类型的所有实例
	Iterable<T> findAll();
    // 返回指定 ID 类型的所有实例
	Iterable<T> findAll(Iterable<ID> ids);
    // 返回可用实体数
	long count();
    // 删除指定 ID 的实体
	void delete(ID id);
    // 删除实体
	void delete(T entity);
    // 删除实体集
	void delete(Iterable<? extends T> entities);
    // 删除所有实体
	void deleteAll();
}
```

**测试方法**

PersonCrudRepository 类

```java
public interface PersonCrudRepository extends CrudRepository<Person, Integer> {}
```

PersonService 类

```java
@Service
public class PersonService
{
    @Autowired
    private PersonCrudRepository personCrudRepository;

    @Transactional
    public void savePersons(List<Person> personList) {
        personCrudRepository.save(personList);
    }
}
```

Test 类

```java
@Test
public void testSavePersons() {
    List<Person> personList = new ArrayList<>();
    Person person;
    for (int i = 'a'; i < 'z'; i++) {
        person = new Person();
        person.setAddressId(i);
        person.setBirthDay(new Date());
        person.setLastName((char) i + "" + (char) i);
        person.setEmail((char) i + "" + (char) i + "@qq.com");
        personList.add(person);
    }
    service.savePersons(personList);
}
```

日志信息

```sql
Hibernate: 
    insert 
    into
        jpa_persons
        (add_id, address_id, birth_day, email, last_name) 
    values
        (?, ?, ?, ?, ?)
# ...
```

数据表 `JPA_PERSONS` 数据

![image-20220526223725209](https://s2.loli.net/2022/05/26/4eFPtViJnyUYkvo.png)



## 3、PagingAndSortingRepository 接口

`PagingAndSortingRepository` 方法一览

![image-20220526225751913](https://s2.loli.net/2022/05/26/c9soBrnPNzX1g5j.png)

`PagingAndSortingRepository` 源码解析

```java
@NoRepositoryBean
public interface PagingAndSortingRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
	// 返回按给定选项排序的所有实体
	Iterable<T> findAll(Sort sort);
	// 返回一个满足分页限制的分页对象
	Page<T> findAll(Pageable pageable);
}
```

**测试方法**

PersonPagingAndSortingRepository 类

```java
public interface PersonPagingAndSortingRepository extends PagingAndSortingRepository<Person, Integer> {}
```

Test 类

```java
@Test
public void testPagingAndSorting() {
    int pageNo = 2; // 从 0 开始
    int pageSize = 10;
    Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"), new Sort.Order(Sort.Direction.DESC, "id"));
    Pageable pageable = new PageRequest(pageNo, pageSize, sort);
    Page<Person> page = repository.findAll(pageable);
    // 格式化输出
    PrintPersonUtil.print(page);
}
```

为了更直观且美观地显示 Person 信息，这里我封装了一个输出 Person 信息的类

```java
public class PrintPersonUtil
{
    public static void print(Page<Person> page) {
        printPageInfo(page);
        printPersonInfo(page.getContent());
    }

    private static void printPageInfo(Page<Person> page) {
        System.out.println("总计：" + page.getTotalElements() + "条");
        System.out.println("总页数：" + page.getTotalPages() + "页");
        System.out.println("当前页：第" + (page.getNumber() + 1) + "页");
        System.out.println("当前页容量：" + page.getSize() + "条");
        System.out.println("当前页实际数：" + page.getNumberOfElements() + "条");
        System.out.println("当前页内容：");
    }

    private static void printPersonInfo(List<Person> personList) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow(Person.ID, Person.ADDRESSID, Person.LASTNAME, Person.EMAIL);
        at.addRule();
        for (Person person : personList) {
            at.addRow(person.getId(), person.getAddressId(), person.getLastName(), person.getEmail());
            at.addRule();
        }
        System.out.println(at.render());
    }
}
```

此类引入了一个如下依赖

```xml
<dependency>
    <groupId>de.vandermeer</groupId>
    <artifactId>asciitable</artifactId>
    <version>0.3.2</version>
</dependency>
```

日志信息

```sql
Hibernate: 
    select
        count(*) as col_0_0_ 
    from
        jpa_persons person0_
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
    order by
        person0_.id asc,
        person0_.id desc limit ?,
        ?
总计：25条
总页数：3页
当前页：第3页
当前页容量：10条
当前页实际数：5条
当前页内容：
┌───────────────────┬───────────────────┬───────────────────┬──────────────────┐
│id                 │addressId          │lastName           │email             │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│223                │110                │nn                 │nn@qq.com         │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│224                │120                │xx                 │xx@qq.com         │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│225                │117                │uu                 │uu@qq.com         │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│316                │116                │tt                 │tt@qq.com         │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│322                │107                │kk                 │kk@qq.com         │
└───────────────────┴───────────────────┴───────────────────┴──────────────────┘
```



## 4、JpaRepository 接口

`JpaRepository` 方法一览

![image-20220527202201285](https://s2.loli.net/2022/05/27/cLidPTy5hJM8nYq.png)

`JpaRepository` 源码解析

```java
@NoRepositoryBean
public interface JpaRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {
	List<T> findAll();
	List<T> findAll(Sort sort);
	List<T> findAll(Iterable<ID> ids);
	<S extends T> List<S> save(Iterable<S> entities);
	// 刷新对数据库的所有挂起更改
	void flush();
	// 保存实体并立即刷新更改
	T saveAndFlush(T entity);
	// 批量删除给定的实体
	void deleteInBatch(Iterable<T> entities);
	// 删除批量调用中的所有实体
	void deleteAllInBatch();
}
```

**测试方法 1**

PersonRepository 类

```java
public interface PersonJpaRepository extends JpaRepository<Person, Integer> {}
```

PersonService 类

```java
@Service
public class PersonService
{
    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Transactional
    public Person saveAndFlushPerson(Person person) {
        return personJpaRepository.saveAndFlush(person);
    }
}
```

Test 类

```java
@Test
public void testSaveAndFlush() {
    Person person = new Person();
    person.setLastName("xy");
    person.setEmail("xy@qq.com");
    person.setBirthDay(new Date());
    service.saveAndFlushPerson(person);
}
```

日志信息

```sql
Hibernate: 
    insert 
    into
        jpa_persons
        (add_id, address_id, birth_day, email, last_name) 
    values
        (?, ?, ?, ?, ?)
```

数据表 `JPA_PERSONS` 数据

![image-20220527202743847](https://s2.loli.net/2022/05/27/J1ry47AGcQb8MzP.png)

**测试方法 2**

Test 类

```java
@Test
public void testSaveAndFlush() {
    Person person = new Person();
    person.setLastName("xyyy");
    person.setEmail("xyyy@qq.com");
    person.setBirthDay(new Date());
    person.setId(324);
    Person person2 = service.saveAndFlushPerson(person);
    System.out.println("person == person2: " + (person == person2));
}
```

日志信息

```sql
Hibernate: 
    select
        person0_.id as id1_1_0_,
        person0_.add_id as add_id6_1_0_,
        person0_.address_id as address_2_1_0_,
        person0_.birth_day as birth_da3_1_0_,
        person0_.email as email4_1_0_,
        person0_.last_name as last_nam5_1_0_ 
    from
        jpa_persons person0_ 
    where
        person0_.id=?
Hibernate: 
    update
        jpa_persons 
    set
        add_id=?,
        address_id=?,
        birth_day=?,
        email=?,
        last_name=? 
    where
        id=?
person == person2: false 
```

数据表 `JPA_PERSONS` 数据

![image-20220527202932667](https://s2.loli.net/2022/05/27/wOt3PKIyQ9jVHur.png)

> **小结**：`JpaRepository` 接口中 `saveAndFlush` 方法相当于 JPA API 中 EntityManager 对象中的 merge 方法



## 5、JpaSpecificationExecutor 接口

不属于 Repository 体系，实现一组 JPA Criteria 查询相关的方法

`JpaSpecificationExecutor` 方法一览

![image-20220527203906391](https://s2.loli.net/2022/05/27/Fu3w7vc1NIGqjAD.png)

`JpaSpecificationExecutor` 源码解析

```java
public interface JpaSpecificationExecutor<T> {
	// 返回满足匹配条件的单个实体
	T findOne(Specification<T> spec);
	// 返回满足匹配条件的所有实体
	List<T> findAll(Specification<T> spec);
	// 返回满足匹配条件的Page对象
	Page<T> findAll(Specification<T> spec, Pageable pageable);
	// 返回满足匹配条件和排序的实体集
	List<T> findAll(Specification<T> spec, Sort sort);
	// 返回满足匹配条件的实例数
	long count(Specification<T> spec);
}
```

其中 `Specification`：封装 JPA Criteria 查询条件。通常使用匿名内部类的方式来创建该接口的对象

```java
public interface Specification<T> {
	// 根据给定的 Criteria 创建一个 WHERE 子句，返回一个 Predicate 作为查询条件 
	Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
```

**测试方法**

> **目标**：实现带查询条件的分页，id > ? 的条件
>
> 调用 JpaSpecificationExecutor 的 `Page<T> findAll(Specification<T> spec, Pageable pageable);` 方法
>
> `Specification`：封装了 JPA Criteria 的查询条件
>
> `Pageable`：封装了请求分页的信息，例如 pageNo，pageSize，Sort

PersonJpaSpecificationExecutor 类

```java
public interface PersonJpaSpecificationExecutor extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {}
```

> **踩坑**：这里注意不能只继承 `JpaSpecificationExecutor` 接口，还要至少实现 Repository 接口或其子接口才行

Test 类

```java
@Test
public void testJpaSpecificationExecutor() {
    // specification：通常使用其匿名内部类
    Specification<Person> specification = new Specification<Person>()
    {
        /**
         * @param *root：代表查询到的类
         * @param query：可以从中获取 Root 对象，即告知 JPA Criteria 要查询哪一个实体类。
         *             还可以来添加查询条件，还可以结合 EntityManager 对象得到最终查询的 TypedQuery 对象。
         * @param *cb：CriteriaBuilder 对象。用于创建 Criteria 相关对象的工厂，从中获取 Predicate 对象
         * @return *Predicate类型，代表一个查询条件
         */
        @Override
        public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Path<Integer> path = root.get("id");
            Predicate predicate = cb.gt(path, 210);
            return predicate;
        }
    };
    // pageable
    Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, Person.ADDRESSID),
                         new Sort.Order(Sort.Direction.ASC, Person.ID));
    Pageable pageable = new PageRequest(2, 5, sort);
    // page
    Page<Person> page = executor.findAll(specification, pageable);
    // 格式化输出
    PrintPersonUtil.print(page);
}
```

其中 root 就是 *导航树* 的 *根*（即查询到的类），由此可以 *导航* 到其 *分支*（即类的属性）

![image-20220527221753652](https://s2.loli.net/2022/05/28/Smj5xivwO7HRDFL.png)

日志信息

```sql
Hibernate: 
    select
        count(*) as col_0_0_ 
    from
        jpa_persons person0_ 
    where
        person0_.id>210
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
        person0_.id>210 
    order by
        person0_.address_id asc,
        person0_.id asc limit ?,
        ?
总计：15条
总页数：3页
当前页：第3页
当前页容量：5条
当前页实际数：5条
当前页内容：
┌───────────────────┬───────────────────┬───────────────────┬──────────────────┐
│id                 │addressId          │lastName           │email             │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│222                │112                │pp                 │pp@qq.com         │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│221                │114                │rr                 │rr@qq.com         │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│316                │116                │tt                 │tt@qq.com         │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│225                │117                │uu                 │uu@qq.com         │
├───────────────────┼───────────────────┼───────────────────┼──────────────────┤
│224                │120                │xx                 │xx@qq.com         │
└───────────────────┴───────────────────┴───────────────────┴──────────────────┘
```

> **踩坑**：本来打算对匿名内部类使用 lambda 表达式进行简化，但执行时报错了，才知道 lambda 表达式是有前提条件的，即：接口必须是<mark>函数式接口</mark>
>
> *函数式接口* 需要满足两个条件：
>
> - 使用 `@FunctionalInterface` 注解修饰
> - 接口中有且仅有一个抽象方法
>
> 而 `Specification` 虽然有且仅有一个抽象方法，但并没有使用 `@FunctionalInterface` 注解修饰，所以不能进行 lambda 简化行为



## 6、自定义 Repository 方法

- 为某个 Repository 添加自定义方法
- 为所有 Repository 添加自定义方法（用的不多，略）

为某个 Repository 添加自定义方法

**步骤**：

- 1）定义一个接口：声明要添加的，并自实现的方法
- 2）提供该接口的实现类：类名需在要声明的 Repository 后添加 Impl，并实现方法
- 3）声明 Repository 接口，并继承 1）声明的接口
- 4）使用

**注意**：默认情况下，Spring Data 会在 `base-package` 中查找“接口名Impl”作为实现类，也可以通过 `repository-impl-postfix` 声明后缀

![image-20220527231650504](https://s2.loli.net/2022/05/28/Ee1LQnMf3X4gOa8.png)

**测试方法**

PersonCustomDao 类

```java
interface PersonCustomDao
{
    Person findById(Integer id);
}
```

PersonCustomDaoImpl 类

```java
class PersonCustomDaoImpl implements PersonCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Person findById(Integer id) {
        return entityManager.find(Person.class, id);
    }
}
```

PersonCustomRepository 类

```java
public interface PersonCustomRepository
    extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person>, PersonCustomDao {}
```

> **注意**：这里除了实现自定义接口 PersonCustomDao 之外，`JpaRepository` 接口也是必不可少的（前面 *踩坑* 提到过）
>
> 那为什么又要实现 `JpaSpecificationExecutor` 呢？其实这个并不是必须的，但是自定义接口目的是在基本的 CRUD、分页等行为仍不满足要求时进行弥补。另外，同时继承 `JpaRepository` 和 `JpaSpecificationExecutor` 也是很常用的写法，使该接口具备通用的数据访问和操作能力

Test 类

```java
@Test
public void testCustomRepository() {
    Person person = repository.findById(221);
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
        person0_.id=?
Person{id=221, addressId=114, lastName='rr', email='rr@qq.com', birthDay=2022-05-26 22:34:33.0}
```



## 总结

本节主要掌握 Repository 的几个子接口

- `CrudRepository`：增删改查方法
- `PagingAndSortingRepository`：分页查询方法
- `JpaRepository`：JPA 规范方法

另外，还有两个比较特殊的接口

- `JpaSpecificationExecutor`：带查询的分页
- 自定义的 Repository：自实现接口方法

附上导图，仅供参考

![03-Repository 子接口](https://s2.loli.net/2022/05/28/hLUatRFGXbT9om7.png)

package helloworld;

import com.vectorx.jpa.helloworld.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

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

    /**
     * 类似于 Hibernate 中 Session 的 load() 方法
     */
    @Test
    public void testGetReference() {
        Customer customer = entityManager.getReference(Customer.class, 2);
        System.out.println(customer.getClass().getName());
        System.out.println("-----------------------------");
        //entityTransaction.commit();
        //entityManager.close();
        System.out.println(customer);
    }

    /**
     * 类似于 Hibernate 中 Session 的 save 方法，使对象有临时状态变为持久化状态
     * 和 Hibernate 中 Session 的 save 方法不同：若对象有 id，则不能执行 insert 操作，而会抛出异常
     */
    @Test
    public void testPersistence() {
        Customer customer = new Customer();
        customer.setLastName("Vector4");
        customer.setEmail("vector4@qq.com");
        customer.setAge(4);
        customer.setBirthDay(new Date());
        customer.setCreateTime(new Date());
        customer.setId(3);
        entityManager.persist(customer);
        System.out.println(customer.getId());
    }

    /**
     * 类似于 Hibernate 中 Session 的 delete() 方法。把对象对应的记录从数据库中移除
     * 需要注意的是，该方法只能移除 持久化对象。而 Hibernate 中 Session 的 delete() 方法还可以移除 游离对象
     */
    @Test
    public void testRemove() {
        //Customer customer = new Customer();
        //customer.setId(4);
        Customer customer = entityManager.find(Customer.class, 4);
        entityManager.remove(customer);
    }

    /**
     * 类似于 Hibernate 中 Session 的 saveOrUpdate() 方法
     * 1、临时对象
     * 创建新的对象，将临时对象属性值复制到新对象中，对新对象执行 insert 持久化操作
     * 所以临时对象没有 id，新对象有 id
     */
    @Test
    public void testMerge1() {
        Customer customer = new Customer();
        customer.setLastName("Vector4");
        customer.setEmail("vector4@qq.com");
        customer.setAge(4);
        customer.setBirthDay(new Date());
        customer.setCreateTime(new Date());
        Customer customer2 = entityManager.merge(customer);
        System.out.println("customer#id: " + customer.getId());
        System.out.println("customer2#id: " + customer2.getId());
    }

    /**
     * 2、游离对象（即传入的对象有 OID）
     * 2.1、EntityManager 缓存中没有该对象 + 数据库中没有对应记录
     * 创建新的对象，将游离对象属性值复制到新对象中，对新对象执行 insert 持久化操作
     */
    @Test
    public void testMerge2() {
        Customer customer = new Customer();
        customer.setLastName("Vector4");
        customer.setEmail("vector4@qq.com");
        customer.setAge(4);
        customer.setBirthDay(new Date());
        customer.setCreateTime(new Date());
        customer.setId(100);
        Customer customer2 = entityManager.merge(customer);
        System.out.println("customer#id: " + customer.getId());
        System.out.println("customer2#id: " + customer2.getId());
    }

    /**
     * 2、游离对象（即传入的对象有 OID）
     * 2.2、EntityManager 缓存中没有该对象 + 数据库中有对应记录
     * JPA 查询对应记录并返回一个对象，将游离对象属性值复制到查询对象中，对查询对象执行 update 持久化操作
     */
    @Test
    public void testMerge3() {
        Customer customer = new Customer();
        customer.setLastName("Vector4");
        customer.setEmail("vector4@qq.com");
        customer.setAge(4);
        customer.setBirthDay(new Date());
        customer.setCreateTime(new Date());
        customer.setId(3);
        Customer customer2 = entityManager.merge(customer);
        System.out.println("customer#id: " + customer.getId());
        System.out.println("customer2#id: " + customer2.getId());
        System.out.println("customer == customer2: " + (customer == customer2));
    }

    /**
     * 2、游离对象（即传入的对象有 OID）
     * 2.3、EntityManager 缓存中有该对象
     * JPA 将游离对象属性值复制到 EntityManager 缓存对象中，对 EntityManager 缓存对象执行 update 持久化操作
     */
    @Test
    public void testMerge4() {
        Customer customer = new Customer();
        customer.setLastName("Vector4");
        customer.setEmail("vector4@qq.com");
        customer.setAge(4);
        customer.setBirthDay(new Date());
        customer.setCreateTime(new Date());
        customer.setId(2);
        Customer customer2 = entityManager.find(Customer.class, 2);
        entityManager.merge(customer);
        System.out.println("customer == customer2: " + (customer == customer2));
    }

    /**
     * 类似于 Hibernate 中 Session 的 flush 方法
     */
    @Test
    public void testFlush() {
        Customer customer = entityManager.find(Customer.class, 3);
        System.out.println(customer);

        customer.setLastName("flush");
        entityManager.flush();

        customer = entityManager.find(Customer.class, 3);
        System.out.println(customer);
    }

    /**
     * 类似于 Hibernate 中 Session 的 refresh 方法
     */
    @Test
    public void testRefresh() {
        Customer customer1 = entityManager.find(Customer.class, 1);
        customer1 = entityManager.find(Customer.class, 1);
        System.out.println("----------------------------");
        Customer customer2 = entityManager.find(Customer.class, 2);
        customer2 = entityManager.find(Customer.class, 2);
        entityManager.refresh(customer2);
    }

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
}

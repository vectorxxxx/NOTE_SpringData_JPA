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
}

package helloworld.unidirectional;

import com.vectorx.jpa.helloworld.unidirectional.many2one.Customer;
import com.vectorx.jpa.helloworld.unidirectional.many2one.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

public class Many2OneTest {
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

    @Test
    public void testPersist() {
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

    /**
     * 建议：在保存多对一的关联关系时，建议先保存 1 的一端，后保存 n 的一端，这样不会多出额外的 update 语句
     */
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

    /**
     * 默认情况下，使用左外连接获取 n 的一端的对象和其关联的 1 的一端的对象
     *
     * @ManyToOne 中 fetch 可修改关联属性的默认加载策略
     */
    @Test
    public void testFind() {
        Order order = entityManager.find(Order.class, 1);
        System.out.println(order.getOrderName());
        System.out.println(order.getCustomer().getLastName());
    }

    @Test
    public void testRemoveN() {
        Order order = entityManager.find(Order.class, 2);
        entityManager.remove(order);
    }

    /**
     * 不能直接删除 1 的一端，因为有外键约束
     */
    @Test
    public void testRemove1() {
        Customer customer = entityManager.find(Customer.class, 9);
        entityManager.remove(customer);
    }

    @Test
    public void testUpdate() {
        Order order = entityManager.find(Order.class, 1);
        order.getCustomer().setLastName("Manny2One-Update");
    }
}

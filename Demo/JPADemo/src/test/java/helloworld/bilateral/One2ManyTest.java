package helloworld.bilateral;

import com.vectorx.jpa.helloworld.bilateral.one2many.Customer;
import com.vectorx.jpa.helloworld.bilateral.one2many.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.Set;

public class One2ManyTest {
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

    @Test
    public void testFind() {
        Customer customer = entityManager.find(Customer.class, 1);
        System.out.println(customer.getLastName());
        System.out.println(customer.getOrders().size());
        Order order = entityManager.find(Order.class, 2);
        System.out.println(order.getOrderName());
        System.out.println(order.getCustomer().getLastName());
    }

    @Test
    public void testRemoveN() {
        Order order = entityManager.find(Order.class, 2);
        entityManager.remove(order);
    }

    @Test
    public void testRemove1() {
        Customer customer = entityManager.find(Customer.class, 1);
        entityManager.remove(customer);
    }

    @Test
    public void testUpdate() {
        Customer customer = entityManager.find(Customer.class, 2);
        customer.getOrders().iterator().next().setOrderName("Customer2-Order1-Update");
    }
}

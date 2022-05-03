package helloworld;

import com.vectorx.jpa.helloworld.bilateral.one2many.Customer;
import com.vectorx.jpa.helloworld.bilateral.one2many.Order;
import org.hibernate.ejb.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;

public class JPQLTest {
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
    public void testAll() {
        String sql = "select c from Customer c where c.age > ?";
        Query query = entityManager.createQuery(sql);
        // 占位符索引是从 1 开始的
        query.setParameter(1, 1);
        List resultList = query.getResultList();
        System.out.println(resultList.size());
    }

    @Test
    public void testPart() {
        String sql = "select c.lastName, c.age from Customer c where c.id = ?";
        List resultList = entityManager.createQuery(sql).setParameter(1, 1).getResultList();
        System.out.println(resultList);
    }

    @Test
    public void testPart2() {
        String sql = "select new Customer(c.lastName, c.age) from Customer c where c.id = ?";
        List resultList = entityManager.createQuery(sql).setParameter(1, 1).getResultList();
        System.out.println(resultList);
    }

    @Test
    public void testNamedQuery(){
        Customer customer = (Customer)entityManager.createNamedQuery("testNamedQuery").setParameter(1, 1).getSingleResult();
        System.out.println(customer);
    }

    @Test
    public void testNativeQuery(){
        String sql = "select age from jpa_customers where id=?";
        Object result = entityManager.createNativeQuery(sql).setParameter(1, 1).getSingleResult();
        System.out.println(result);
    }

    @Test
    public void testQueryCache(){
        String sql = "select c from Customer c where c.id=?";
        // First Query
        //Query query = entityManager.createQuery(sql).setParameter(1, 1);
        Query query = entityManager.createQuery(sql).setParameter(1, 1).setHint(QueryHints.HINT_CACHEABLE, true);
        List resultList = query.getResultList();
        System.out.println(resultList.size());
        // Second Query
        //Query query2 = entityManager.createQuery(sql).setParameter(1, 1);
        Query query2 = entityManager.createQuery(sql).setParameter(1, 1).setHint(QueryHints.HINT_CACHEABLE, true);
        List resultList2 = query2.getResultList();
        System.out.println(resultList2.size());
    }

    @Test
    public void testOrderBy(){
        String sql = "select c from Customer c where c.id=? order by c.age desc";
        List resultList = entityManager.createQuery(sql).setParameter(1, 1).getResultList();
        System.out.println(resultList.size());
    }

    @Test
    public void testGroupBy(){
        String sql = "select o.customer from Order o group by o.customer having count(o.id) >= 2";
        List<Order> resultList = entityManager.createQuery(sql).getResultList();
        System.out.println(resultList);
    }
}

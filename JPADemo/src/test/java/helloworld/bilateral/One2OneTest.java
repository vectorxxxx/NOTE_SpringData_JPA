package helloworld.bilateral;

import com.vectorx.jpa.helloworld.bilateral.one2one.Department;
import com.vectorx.jpa.helloworld.bilateral.one2one.Manager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class One2OneTest {
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
        Manager mgr = new Manager();
        mgr.setMgrName("Mgr1");
        Department dept = new Department();
        dept.setDeptName("Dept1");

        // 设置 1-1 关联关系
        mgr.setDept(dept);
        dept.setMgr(mgr);

        entityManager.persist(mgr);
        entityManager.persist(dept);
    }

    @Test
    public void testPersist2() {
        Manager mgr = new Manager();
        mgr.setMgrName("Mgr2");
        Department dept = new Department();
        dept.setDeptName("Dept2");

        // 设置 1-1 关联关系
        mgr.setDept(dept);
        dept.setMgr(mgr);

        entityManager.persist(dept);
        entityManager.persist(mgr);
    }

    @Test
    public void testFind1() {
        Department dept = entityManager.find(Department.class, 1);
        System.out.println(dept.getDeptName());
        System.out.println(dept.getMgr().getClass().getName());
    }

    @Test
    public void testFind2() {
        Manager mgr = entityManager.find(Manager.class, 1);
        System.out.println(mgr.getMgrName());
        System.out.println(mgr.getDept().getClass().getName());
    }
}

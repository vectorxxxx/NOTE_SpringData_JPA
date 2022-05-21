package helloworld.bilateral;

import com.vectorx.jpa.helloworld.bilateral.many2many.Category;
import com.vectorx.jpa.helloworld.bilateral.many2many.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Many2ManyTest {
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
        // 商品
        Item i1 = new Item();
        i1.setItemName("i-1");
        Item i2 = new Item();
        i2.setItemName("i-2");
        // 类别
        Category c1 = new Category();
        c1.setCategoryName("c-1");
        Category c2 = new Category();
        c2.setCategoryName("c-2");

        // 设置双向 n-n 关联关系——Category
        i1.getCategories().add(c1);
        i1.getCategories().add(c2);
        i2.getCategories().add(c1);
        i2.getCategories().add(c2);
        // 设置双向 n-n 关联关系——Item
        c1.getItems().add(i1);
        c1.getItems().add(i2);
        c2.getItems().add(i1);
        c2.getItems().add(i2);

        entityManager.persist(i1);
        entityManager.persist(i2);
        entityManager.persist(c1);
        entityManager.persist(c2);
    }

    @Test
    public void testFind1() {
        Item item = entityManager.find(Item.class, 1);
        System.out.println(item.getItemName());
        System.out.println(item.getCategories().size());
    }

    @Test
    public void testFind2() {
        Category category = entityManager.find(Category.class, 1);
        System.out.println(category.getCategoryName());
        System.out.println(category.getItems().size());
    }
}

package com.vectorx.springdata;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.repository.PersonJpaSpecificationExecutor;
import com.vectorx.springdata.util.PrintPersonUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

/**
 * SpringData 测试类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:28:32
 */
public class Test07_JpaSpecificationExecutor
{
    private ApplicationContext context;
    private PersonJpaSpecificationExecutor executor;

    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        executor = context.getBean(PersonJpaSpecificationExecutor.class);
    }

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

    // 不满足函数式接口，无法使用 lambda 表达式进行简化 -- add by vectorx
    // @Deprecated
    // @Test
    // public void testJpaSpecificationExecutor2() {
    //     // specification
    //     Specification<Person> specification = (root, query, cb) -> cb.gt(root.get("id"), 210);
    //     // pageable
    //     Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, Person.ADDRESSID),
    //             new Sort.Order(Sort.Direction.ASC, Person.ID));
    //     Pageable pageable = new PageRequest(2, 5, sort);
    //     // page
    //     Page<Person> page = executor.findAll(specification, pageable);
    //     // 格式化输出
    //     PrintPersonUtil.print(page);
    // }
}

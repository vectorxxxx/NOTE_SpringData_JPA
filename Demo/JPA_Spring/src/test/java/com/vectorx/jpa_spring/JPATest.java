package com.vectorx.jpa_spring;

import com.vectorx.jpa_spring.entities.Person;
import com.vectorx.jpa_spring.service.PersonService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 测试
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-20 22:35:44
 */
public class JPATest
{
    private ApplicationContext context;
    private PersonService personService;
    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        personService = context.getBean(PersonService.class);
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testPersonService(){
        Person p1 = new Person();
        p1.setLastName("AA");
        p1.setAge(11);
        p1.setEmail("AA@qq.com");
        Person p2 = new Person();
        p2.setLastName("BB");
        p2.setAge(22);
        p2.setEmail("BB@qq.com");

        System.out.println(personService.getClass().getName());
        personService.savePerson(p1, p2);
    }
}

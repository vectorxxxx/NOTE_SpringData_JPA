package com.vectorx.springdata;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.repository.PersonRepository;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * SpringData 测试类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:28:32
 */
public class SpringDataTest
{
    private ApplicationContext context;
    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testPersonRepository(){
        PersonRepository repository = context.getBean(PersonRepository.class);
        Person person = repository.getByLastName("AA");
        System.out.println(person);
    }
}

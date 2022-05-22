package com.vectorx.springdata;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.repository.PersonRepository;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private PersonRepository repository;
    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        repository = context.getBean(PersonRepository.class);
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testPersonRepository() {
        Person person = repository.getByLastName("AA");
        System.out.println(person);
    }

    @Test
    public void testFindByLastNameEndingWithAndIdLessThan() {
        List<Person> personList = repository.findByLastNameStartingWithAndIdLessThan("X", 30);
        System.out.println(personList);
    }

    @Test
    public void testFindByEmailInAndBirthDayBetween() {
        List<String> emailList = Arrays.asList("AA@qq.com", "CC@qq.com");
        List<Person> personList = repository.findByEmailInAndBirthDayLessThan(emailList, new Date());
        System.out.println(personList);
    }

    @Test
    public void testFindByAddressId() {
        List<Person> personList = repository.findByAddressId(123);
        System.out.println(personList);
    }

    @Test
    public void testFindByAddress_Id() {
        List<Person> personList = repository.findByAddress_Id(123);
        System.out.println(personList);
    }
}

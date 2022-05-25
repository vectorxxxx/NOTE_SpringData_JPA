package com.vectorx.springdata;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.repository.PersonRepository;
import com.vectorx.springdata.service.PersonService;
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
    private PersonService service;
    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        repository = context.getBean(PersonRepository.class);
        service = context.getBean(PersonService.class);
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

    @Test
    public void testGetMaxIdPerson() {
        Person person = repository.getMaxIdPerson();
        System.out.println(person);
    }

    @Test
    public void testGetByLastNameAndEmail() {
        Person person = repository.getByLastNameAndEmail("AA", "AA@qq.com");
        System.out.println(person);
    }

    @Test
    public void testGetByEmailAndLastName() {
        Person person = repository.getByEmailAndLastName("BB@qq.com", "BB");
        System.out.println(person);
    }

    @Test
    public void testFindLikeLastNameOrEmail() {
        List<Person> personList = repository.findLikeLastNameOrEmail("A", "BB");
        System.out.println(personList);
    }

    @Test
    public void testFindLikeEmailOrLastName() {
        List<Person> personList = repository.findLikeEmailOrLastName("DD", "C");
        System.out.println(personList);
    }

    @Test
    public void testGetTotalCount() {
        int totalCount = repository.getTotalCount();
        System.out.println(totalCount);
    }

    @Test
    public void testUpdateEmailById() {
        // int updateCount = repository.updateEmailById("aaaa@qq.com", 1);
        // System.out.println(updateCount);
        int updateCount = service.updateEmailById("aaaa@qq.com", 1);
        System.out.println(updateCount);
    }
}

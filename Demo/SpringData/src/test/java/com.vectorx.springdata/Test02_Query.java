package com.vectorx.springdata;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.repository.PersonRepository;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * SpringData 测试类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:28:32
 */
public class Test02_Query
{
    private ApplicationContext context;
    private PersonRepository repository;

    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        repository = context.getBean(PersonRepository.class);
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
}

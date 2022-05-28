package com.vectorx.springdata;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.service.PersonService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SpringData 测试类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:28:32
 */
public class Test04_CrudRepository
{
    private ApplicationContext context;
    private PersonService service;

    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = context.getBean(PersonService.class);
    }

    @Test
    public void testSavePersons() {
        List<Person> personList = new ArrayList<>();
        Person person;
        for (int i = 'a'; i < 'z'; i++) {
            person = new Person();
            person.setAddressId(i);
            person.setBirthDay(new Date());
            person.setLastName((char) i + "" + (char) i);
            person.setEmail((char) i + "" + (char) i + "@qq.com");
            personList.add(person);
        }
        service.savePersons(personList);
    }
}

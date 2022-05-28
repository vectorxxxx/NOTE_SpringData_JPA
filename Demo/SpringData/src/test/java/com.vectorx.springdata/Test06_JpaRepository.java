package com.vectorx.springdata;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.service.PersonService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * SpringData 测试类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:28:32
 */
public class Test06_JpaRepository
{
    private ApplicationContext context;
    private PersonService service;

    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = context.getBean(PersonService.class);
    }

    @Test
    public void testSaveAndFlush() {
        Person person = new Person();
        person.setLastName("xyyy");
        person.setEmail("xyyy@qq.com");
        person.setBirthDay(new Date());
        person.setId(324);
        Person person2 = service.saveAndFlushPerson(person);
        System.out.println("person == person2: " + (person == person2));
    }
}

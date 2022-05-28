package com.vectorx.springdata;

import com.vectorx.springdata.custom.PersonCustomRepository;
import com.vectorx.springdata.entities.Person;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringData 测试类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-27 23:27:44
 */
public class Test08_CustomRepository
{
    private ApplicationContext context;
    private PersonCustomRepository repository;

    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        repository = context.getBean(PersonCustomRepository.class);
    }

    @Test
    public void testCustomRepository() {
        Person person = repository.findById(221);
        System.out.println(person);
    }
}

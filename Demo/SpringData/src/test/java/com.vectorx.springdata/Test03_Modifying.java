package com.vectorx.springdata;

import com.vectorx.springdata.repository.PersonRepository;
import com.vectorx.springdata.service.PersonService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * SpringData 测试类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:28:32
 */
public class Test03_Modifying
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
    public void testUpdateEmailById1() {
        int updateCount = repository.updateEmailById("aaaa@qq.com", 1);
        System.out.println(updateCount);
    }

    @Test
    public void testUpdateEmailById2() {
        int updateCount = service.updateEmailById("aaaa@qq.com", 1);
        System.out.println(updateCount);
    }
}

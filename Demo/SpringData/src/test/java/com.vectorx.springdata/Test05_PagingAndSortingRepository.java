package com.vectorx.springdata;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.repository.PersonPagingAndSortingRepository;
import com.vectorx.springdata.util.PrintPersonUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * SpringData 测试类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:28:32
 */
public class Test05_PagingAndSortingRepository
{
    private ApplicationContext context;
    private PersonPagingAndSortingRepository repository;

    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        repository = context.getBean(PersonPagingAndSortingRepository.class);
    }

    @Test
    public void testPagingAndSorting() {
        int pageNo = 2;
        int pageSize = 10;
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"), new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(pageNo, pageSize, sort);
        Page<Person> page = repository.findAll(pageable);
        // 格式化输出
        PrintPersonUtil.print(page);
    }
}

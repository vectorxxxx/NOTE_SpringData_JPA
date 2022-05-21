package com.vectorx.jpa_spring.service;

import com.vectorx.jpa_spring.dao.PersonDao;
import com.vectorx.jpa_spring.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 操作Person实体类的Service
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-20 23:08:46
 */
@Service
public class PersonService
{
    @Autowired
    private PersonDao personDao;

    @Transactional
    public void savePerson(Person p1, Person p2) {
        personDao.save(p1);
        // 模拟异常
        int i = 10 / 0;
        personDao.save(p2);
    }
}

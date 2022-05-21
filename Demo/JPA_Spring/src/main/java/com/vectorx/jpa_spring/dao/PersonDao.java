package com.vectorx.jpa_spring.dao;

import com.vectorx.jpa_spring.entities.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 操作Person实体类的Dao
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-20 23:06:53
 */
@Repository
public class PersonDao
{
    @PersistenceContext
    private EntityManager entityManager;

    public void save(Person p){
        entityManager.persist(p);
    }
}

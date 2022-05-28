package com.vectorx.springdata.custom;

import com.vectorx.springdata.entities.Person;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * PersonCustomDaoImpl
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-27 23:24:34
 */
class PersonCustomDaoImpl implements PersonCustomDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Person findById(Integer id) {
        return entityManager.find(Person.class, id);
    }
}

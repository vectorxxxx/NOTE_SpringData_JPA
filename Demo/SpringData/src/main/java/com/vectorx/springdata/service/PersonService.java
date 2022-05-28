package com.vectorx.springdata.service;

import com.vectorx.springdata.entities.Person;
import com.vectorx.springdata.repository.PersonCrudRepository;
import com.vectorx.springdata.repository.PersonJpaRepository;
import com.vectorx.springdata.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Person 对应的 Service
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-25 21:39:08
 */
@Service
public class PersonService
{
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonCrudRepository personCrudRepository;
    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Transactional
    public int updateEmailById(String email, Integer id) {
        return personRepository.updateEmailById(email, id);
    }

    @Transactional
    public void savePersons(List<Person> personList) {
        personCrudRepository.save(personList);
    }

    @Transactional
    public Person saveAndFlushPerson(Person person) {
        return personJpaRepository.saveAndFlush(person);
    }
}

package com.vectorx.springdata.service;

import com.vectorx.springdata.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public int updateEmailById(String email, Integer id) {
        return personRepository.updateEmailById(email, id);
    }
}

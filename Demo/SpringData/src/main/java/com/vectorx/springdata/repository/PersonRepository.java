package com.vectorx.springdata.repository;

import com.vectorx.springdata.entities.Person;
import org.springframework.data.repository.Repository;

/**
 * PersonRepository
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:42:41
 */
public interface PersonRepository extends Repository<Person, Integer>
{
    Person getByLastName(String lastName);
}

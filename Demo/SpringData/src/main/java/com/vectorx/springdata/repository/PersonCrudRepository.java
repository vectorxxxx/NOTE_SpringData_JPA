package com.vectorx.springdata.repository;

import com.vectorx.springdata.entities.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * PersonCrudRepository
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:42:41
 */
public interface PersonCrudRepository extends CrudRepository<Person, Integer>
{
}

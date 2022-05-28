package com.vectorx.springdata.repository;

import com.vectorx.springdata.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * PersonJpaSpecificationExecutor
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:42:41
 */
// public interface PersonJpaSpecificationExecutor extends Repository<Person, Integer>, JpaSpecificationExecutor<Person>
public interface PersonJpaSpecificationExecutor extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person>
{
}

package com.vectorx.springdata.custom;

import com.vectorx.springdata.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * PersonCustomRepository
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-27 23:26:05
 */
public interface PersonCustomRepository
        extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person>, PersonCustomDao
{
}

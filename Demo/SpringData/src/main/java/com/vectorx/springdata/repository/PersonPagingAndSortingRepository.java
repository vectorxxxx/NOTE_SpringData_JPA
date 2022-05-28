package com.vectorx.springdata.repository;

import com.vectorx.springdata.entities.Person;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * PersonPagingAndSortingRepository
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:42:41
 */
public interface PersonPagingAndSortingRepository extends PagingAndSortingRepository<Person, Integer>
{
}

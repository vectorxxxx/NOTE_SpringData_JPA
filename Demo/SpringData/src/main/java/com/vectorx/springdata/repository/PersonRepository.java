package com.vectorx.springdata.repository;

import com.vectorx.springdata.entities.Person;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.List;

/**
 * PersonRepository
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-21 22:42:41
 */
// @RepositoryDefinition(domainClass = Person.class, idClass = Integer.class)
public interface PersonRepository extends Repository<Person, Integer>
{
    Person getByLastName(String lastName);

    List<Person> findByLastNameStartingWithAndIdLessThan(String lastname, Integer id);

    List<Person> findByEmailInAndBirthDayLessThan(List<String> emailList, Date birthDay);

    List<Person> findByAddressId(Integer addressId);

    List<Person> findByAddress_Id(Integer addressId);
}

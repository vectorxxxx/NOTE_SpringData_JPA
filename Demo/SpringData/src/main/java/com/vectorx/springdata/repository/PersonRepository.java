package com.vectorx.springdata.repository;

import com.vectorx.springdata.entities.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

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

    @Query("select p from Person p where p.id=(select max(id) from Person p)")
    Person getMaxIdPerson();

    @Query("select p from Person p where p.lastName=?1 and p.email=?2")
    Person getByLastNameAndEmail(String lastName, String email);

    @Query("select p from Person p where p.lastName=:lastName and p.email=:email")
    Person getByEmailAndLastName(@Param("email") String email, @Param("lastName") String lastName);

    @Query("select p from Person p where p.lastName like %?1% or p.email like %?2%")
    List<Person> findLikeLastNameOrEmail(String lastName, String email);

    @Query("select p from Person p where p.lastName like %:lastName% or p.email like %:email%")
    List<Person> findLikeEmailOrLastName(@Param("email") String email, @Param("lastName") String lastName);

    @Query(value = "select count(1) FROM jpa_persons", nativeQuery = true)
    int getTotalCount();

    @Modifying
    @Query("update Person p set p.email=:email where p.id=:id")
    int updateEmailById(@Param("email") String email, @Param("id") Integer id);
}

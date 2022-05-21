package com.vectorx.springdata.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * 人的实体类
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-20 22:57:58
 */
@Table(name = "JPA_PERSONS")
@Entity
public class Person
{
    private Integer id;
    private String lastName;
    private String email;
    private Date birthDay;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", birthDay=" + birthDay + '}';
    }
}

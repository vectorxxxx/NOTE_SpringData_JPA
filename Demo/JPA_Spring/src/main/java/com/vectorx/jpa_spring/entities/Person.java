package com.vectorx.jpa_spring.entities;

import javax.persistence.*;

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
    private int age;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "LAST_NAME")
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

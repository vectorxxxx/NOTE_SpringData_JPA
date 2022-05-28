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
    public static final String ID = "id";
    public static final String LASTNAME = "lastName";
    public static final String EMAIL = "email";
    public static final String BIRTHDAY = "birthDay";
    public static final String ADDRESSID = "addressId";

    private Integer id;
    private String lastName;
    private String email;
    private Date birthDay;
    private Address address;
    private Integer addressId;

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

    @JoinColumn(name = "ADD_ID")
    @ManyToOne
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", addressId=" + addressId + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", birthDay=" + birthDay + '}';
    }
}

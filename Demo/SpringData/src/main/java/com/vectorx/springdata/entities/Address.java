package com.vectorx.springdata.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 地址
 *
 * @author vectorx
 * @version 1.0
 * @date 2022-05-22 19:47:40
 */
@Table(name = "JPA_ADDESSS")
@Entity
public class Address
{
    private Integer id;
    private String country;
    private String city;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

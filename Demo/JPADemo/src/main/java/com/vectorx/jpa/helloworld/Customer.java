package com.vectorx.jpa.helloworld;

import javax.persistence.*;
import java.util.Date;

//@NamedQuery(name = "testNamedQuery", query = "select c from Customer c where c.id = ?")
// 二级缓存相关
//@Cacheable(value = true)
// 标识为持久化类
//@Entity
// 设置关联数据表
//@Table(name = "JPA_CUSTOMERS")
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;

    private Date birthDay;
    private Date createTime;

    public Customer() {
    }

    public Customer(String lastName, int age) {
        this.lastName = lastName;
        this.age = age;
    }

    //@TableGenerator(
    //        name = "ID_GENERATOR",
    //        table = "JPA_ID_GENERATOR",
    //        pkColumnName = "PK_NAME",
    //        pkColumnValue = "CUSTOMER_ID",
    //        valueColumnName = "PK_VALUE",
    //        allocationSize = 100
    //)
    //@GeneratedValue(strategy = GenerationType.TABLE, generator = "ID_GENERATOR")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // 设置关联字段名，若同名可省略
    @Column(name = "LAST_NAME", length = 50, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
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

    @Temporal(TemporalType.DATE)
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    // 工具方法，不需要映射为数据表的一列
    @Transient
    public String getInfo() {
        return "lastName: " + this.lastName + ", email: " + email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", birthDay=" + birthDay +
                ", createTime=" + createTime +
                '}';
    }
}

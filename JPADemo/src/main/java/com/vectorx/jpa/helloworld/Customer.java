package com.vectorx.jpa.helloworld;

import javax.persistence.*;

// 标识为持久化类
@Entity
// 设置关联数据表
@Table(name = "JPA_CUSTOMERS")
public class Customer {
    private Integer id;
    private String lastName;
    private String email;
    private int age;

    // 标识为主键
    @Id
    // 设置生成策略为 AUTO
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // 设置关联字段名，若同名可省略
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

package com.vectorx.jpa.helloworld.unidirectional.many2one;

import javax.persistence.*;

//@Table(name = "JPA_ORDER")
//@Entity
public class Order {
    private Integer id;
    private String orderName;
    private Customer customer;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ORDER_NAME")
    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    // @JoinColumn 映射外键
    @JoinColumn(name = "CUSTOMER_ID")
    // @ManyToOne 映射多对一（n-1）的关联关系
    // - fetch 设置关联属性的默认加载策略
    @ManyToOne(fetch = FetchType.LAZY)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

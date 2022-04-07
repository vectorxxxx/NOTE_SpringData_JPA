package com.vectorx.jpa.helloworld;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        //1、创建 EntityManagerFactory
        String persistenceUnitName = "jpa-1";
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        //2、创建 EntityManager
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        //3、开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        //4、进行持久化操作
        Customer customer = new Customer();
        customer.setLastName("Vector");
        customer.setEmail("vector@qq.com");
        customer.setAge(100);
        entityManager.persist(customer);
        //5、提交事务
        transaction.commit();
        //6、关闭 EntityManager
        entityManager.close();
        //6、关闭 EntityManagerFactory
        entityManagerFactory.close();
    }
}

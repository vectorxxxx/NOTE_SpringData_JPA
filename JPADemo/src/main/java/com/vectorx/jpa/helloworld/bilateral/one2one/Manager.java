package com.vectorx.jpa.helloworld.bilateral.one2one;

import javax.persistence.*;

@Table(name = "JPA_MANAGERS")
@Entity
public class Manager {
    private Integer id;
    private String mgrName;
    private Department dept;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "MGR_NAME")
    public String getMgrName() {
        return mgrName;
    }

    public void setMgrName(String mgrName) {
        this.mgrName = mgrName;
    }

    /**
     * 使用 @OneToOne 注解来映射 1-1 关联关系
     * 使用 mappedBy 属性来不维护关联关系（没有外键的一方）
     */
    //@OneToOne(mappedBy = "mgr", fetch = FetchType.LAZY)
    @OneToOne(mappedBy = "mgr")
    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }
}

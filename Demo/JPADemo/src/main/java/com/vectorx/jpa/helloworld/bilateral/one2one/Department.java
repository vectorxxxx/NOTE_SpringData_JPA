package com.vectorx.jpa.helloworld.bilateral.one2one;

import javax.persistence.*;

@Table(name = "JPA_DEPARTMENTS")
@Entity
public class Department {
    private Integer id;
    private String deptName;
    private Manager mgr;

    @GeneratedValue
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "DEPT_NAME")
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 使用 @OneToOne 注解来映射 1-1 关联关系
     * 使用 @JoinColumn 注解来映射当前数据表的外键
     * 注意：1-1 关联关系需要添加 unique = true
     *
     * @return
     */
    @JoinColumn(name = "MGR_ID", unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    public Manager getMgr() {
        return mgr;
    }

    public void setMgr(Manager mgr) {
        this.mgr = mgr;
    }
}

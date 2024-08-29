package com.coderevolt.model;

import com.coderevolt.sql.attr.Column;
import com.coderevolt.sql.attr.Table;

@Table("student")
public class StudentModel {

    @Column(type = Column.ColumnType.ID)
    private Long id;

    @Column(name = "t_name")
    private String name;

    @Column(dmlStrategy = Column.DmlStrategy.IGNORE_NULL)
    private Integer age;

    @Column(dmlStrategy = Column.DmlStrategy.IGNORE_NULL)
    private String hobby;

    @Column(dmlStrategy = Column.DmlStrategy.SET_NULL)
    private String avatar;

    public StudentModel() {
    }

    public StudentModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StudentModel(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}

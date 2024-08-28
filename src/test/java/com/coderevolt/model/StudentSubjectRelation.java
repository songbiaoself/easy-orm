package com.coderevolt.model;

import com.coderevolt.sql.attr.Table;

@Table("student_subject_relation")
public class StudentSubjectRelation {

    private int id;

    private String studentId;

    private String subjectId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}

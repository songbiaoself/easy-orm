package com.coderevolt.model;


import com.coderevolt.sql.attr.Table;

import java.nio.charset.StandardCharsets;

@Table("subject")
public class SubjectModel {

    private Long id;

    private String name;

    private byte[] book;

    public byte[] getBook() {
        return book;
    }

    public void setBook(byte[] book) {
        this.book = book;
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
        return "SubjectModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", book=" + (book != null ? new String(book, StandardCharsets.UTF_8) : null) +
                '}';
    }

    public SubjectModel() {
    }

    public SubjectModel(Long id, String name, byte[] book) {
        this.id = id;
        this.name = name;
        this.book = book;
    }
}

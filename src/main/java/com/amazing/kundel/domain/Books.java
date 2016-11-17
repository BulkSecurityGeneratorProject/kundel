package com.amazing.kundel.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Books.
 */

@Document(collection = "books")
public class Books implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("isbn")
    private String isbn;

    @Field("year")
    private String year;

    @Field("url_s")
    private String url_s;

    @Field("url_m")
    private String url_m;

    @Field("url_l")
    private String url_l;

    @Field("author")
    private Integer author;

    @Field("publisher")
    private Integer publisher;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Books title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public Books isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getYear() {
        return year;
    }

    public Books year(String year) {
        this.year = year;
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUrl_s() {
        return url_s;
    }

    public Books url_s(String url_s) {
        this.url_s = url_s;
        return this;
    }

    public void setUrl_s(String url_s) {
        this.url_s = url_s;
    }

    public String getUrl_m() {
        return url_m;
    }

    public Books url_m(String url_m) {
        this.url_m = url_m;
        return this;
    }

    public void setUrl_m(String url_m) {
        this.url_m = url_m;
    }

    public String getUrl_l() {
        return url_l;
    }

    public Books url_l(String url_l) {
        this.url_l = url_l;
        return this;
    }

    public void setUrl_l(String url_l) {
        this.url_l = url_l;
    }

    public Integer getAuthor() {
        return author;
    }

    public Books author(Integer author) {
        this.author = author;
        return this;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getPublisher() {
        return publisher;
    }

    public Books publisher(Integer publisher) {
        this.publisher = publisher;
        return this;
    }

    public void setPublisher(Integer publisher) {
        this.publisher = publisher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Books books = (Books) o;
        if(books.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, books.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Books{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", isbn='" + isbn + "'" +
            ", year='" + year + "'" +
            ", url_s='" + url_s + "'" +
            ", url_m='" + url_m + "'" +
            ", url_l='" + url_l + "'" +
            ", author='" + author + "'" +
            ", publisher='" + publisher + "'" +
            '}';
    }
}

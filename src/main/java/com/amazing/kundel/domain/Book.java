package com.amazing.kundel.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Book.
 */

@Document(collection = "booksALL")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("id_book")
    private Integer idBook;

    @Field("isbn")
    private String isbn;

    @Field("title")
    private String title;

    @Field("author")
    private String author;

    @Field("year")
    private String year;

    @Field("publisher")
    private String publisher;

    @Field("url_s")
    private String url_s;

    @Field("url_m")
    private String url_m;

    @Field("url_l")
    private String url_l;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public Book idBook(Integer idBook) {
        this.idBook = idBook;
        return this;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public Book year(String year) {
        this.year = year;
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPublisher() {
        return publisher;
    }

    public Book publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUrl_s() {
        return url_s;
    }

    public Book url_s(String url_s) {
        this.url_s = url_s;
        return this;
    }

    public void setUrl_s(String url_s) {
        this.url_s = url_s;
    }

    public String getUrl_m() {
        return url_m;
    }

    public Book url_m(String url_m) {
        this.url_m = url_m;
        return this;
    }

    public void setUrl_m(String url_m) {
        this.url_m = url_m;
    }

    public String getUrl_l() {
        return url_l;
    }

    public Book url_l(String url_l) {
        this.url_l = url_l;
        return this;
    }

    public void setUrl_l(String url_l) {
        this.url_l = url_l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        if(book.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", idBook='" + idBook + "'" +
            ", isbn='" + isbn + "'" +
            ", title='" + title + "'" +
            ", author='" + author + "'" +
            ", year='" + year + "'" +
            ", publisher='" + publisher + "'" +
            ", url_s='" + url_s + "'" +
            ", url_m='" + url_m + "'" +
            ", url_l='" + url_l + "'" +
            '}';
    }
}

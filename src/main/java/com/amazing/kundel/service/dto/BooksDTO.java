package com.amazing.kundel.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Books entity.
 */
public class BooksDTO implements Serializable {

    private String id;

    private String title;

    private String isbn;

    private String year;

    private String url_s;

    private String url_m;

    private String url_l;

    private Integer author;

    private Integer publisher;

    private Integer idBook;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    public String getUrl_s() {
        return url_s;
    }

    public void setUrl_s(String url_s) {
        this.url_s = url_s;
    }
    public String getUrl_m() {
        return url_m;
    }

    public void setUrl_m(String url_m) {
        this.url_m = url_m;
    }
    public String getUrl_l() {
        return url_l;
    }

    public void setUrl_l(String url_l) {
        this.url_l = url_l;
    }
    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }
    public Integer getPublisher() {
        return publisher;
    }

    public void setPublisher(Integer publisher) {
        this.publisher = publisher;
    }
    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BooksDTO booksDTO = (BooksDTO) o;

        if ( ! Objects.equals(id, booksDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BooksDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", isbn='" + isbn + "'" +
            ", year='" + year + "'" +
            ", url_s='" + url_s + "'" +
            ", url_m='" + url_m + "'" +
            ", url_l='" + url_l + "'" +
            ", author='" + author + "'" +
            ", publisher='" + publisher + "'" +
            ", idBook='" + idBook + "'" +
            '}';
    }
}

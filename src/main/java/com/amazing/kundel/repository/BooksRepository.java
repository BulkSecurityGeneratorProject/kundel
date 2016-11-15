package com.amazing.kundel.repository;

import com.amazing.kundel.domain.Books;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Books entity.
 */
@SuppressWarnings("unused")
public interface BooksRepository extends MongoRepository<Books,String> {

}

package com.amazing.kundel.repository;

import com.amazing.kundel.domain.Authors;
import com.amazing.kundel.domain.Books;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Spring Data MongoDB repository for the Books entity.
 */
@SuppressWarnings("unused")
public interface BooksRepository extends MongoRepository<Books,String> {
    @Query(value = "{ 'id_book' : ?0 }")
    Books findByFuckinId(int id_book);
}

package com.amazing.kundel.repository;

import com.amazing.kundel.domain.Authors;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Spring Data MongoDB repository for the Authors entity.
 */
@SuppressWarnings("unused")
public interface AuthorsRepository extends MongoRepository<Authors,String> {
    @Query(value = "{ 'id_author' : ?0 }")
    Authors findByFuckinId(int id_author);
}

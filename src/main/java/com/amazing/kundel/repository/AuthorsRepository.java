package com.amazing.kundel.repository;

import com.amazing.kundel.domain.Authors;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Authors entity.
 */
@SuppressWarnings("unused")
public interface AuthorsRepository extends MongoRepository<Authors,String> {

}

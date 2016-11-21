package com.amazing.kundel.repository;

import com.amazing.kundel.domain.Transaction;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Transaction entity.
 */
@SuppressWarnings("unused")
public interface TransactionRepository extends MongoRepository<Transaction,String> {

}

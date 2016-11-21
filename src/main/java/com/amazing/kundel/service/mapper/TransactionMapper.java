package com.amazing.kundel.service.mapper;

import com.amazing.kundel.domain.*;
import com.amazing.kundel.service.dto.TransactionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Transaction and its DTO TransactionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransactionMapper {

    TransactionDTO transactionToTransactionDTO(Transaction transaction);

    List<TransactionDTO> transactionsToTransactionDTOs(List<Transaction> transactions);

    Transaction transactionDTOToTransaction(TransactionDTO transactionDTO);

    List<Transaction> transactionDTOsToTransactions(List<TransactionDTO> transactionDTOs);
}

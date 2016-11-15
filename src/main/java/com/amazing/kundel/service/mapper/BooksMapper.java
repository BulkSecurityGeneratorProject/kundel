package com.amazing.kundel.service.mapper;

import com.amazing.kundel.domain.*;
import com.amazing.kundel.service.dto.BooksDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Books and its DTO BooksDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BooksMapper {

    BooksDTO booksToBooksDTO(Books books);

    List<BooksDTO> booksToBooksDTOs(List<Books> books);

    Books booksDTOToBooks(BooksDTO booksDTO);

    List<Books> booksDTOsToBooks(List<BooksDTO> booksDTOs);
}

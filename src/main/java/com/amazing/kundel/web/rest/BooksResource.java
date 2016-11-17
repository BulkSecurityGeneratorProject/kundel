package com.amazing.kundel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amazing.kundel.domain.Books;

import com.amazing.kundel.repository.BooksRepository;
import com.amazing.kundel.web.rest.util.HeaderUtil;
import com.amazing.kundel.web.rest.util.PaginationUtil;
import com.amazing.kundel.service.dto.BooksDTO;
import com.amazing.kundel.service.mapper.BooksMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Books.
 */
@RestController
@RequestMapping("/api")
public class BooksResource {

    private final Logger log = LoggerFactory.getLogger(BooksResource.class);

    @Inject
    private BooksRepository booksRepository;

    @Inject
    private BooksMapper booksMapper;

    /**
     * POST  /books : Create a new books.
     *
     * @param booksDTO the booksDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new booksDTO, or with status 400 (Bad Request) if the books has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/books",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooksDTO> createBooks(@RequestBody BooksDTO booksDTO) throws URISyntaxException {
        log.debug("REST request to save Books : {}", booksDTO);
        if (booksDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("books", "idexists", "A new books cannot already have an ID")).body(null);
        }
        Books books = booksMapper.booksDTOToBooks(booksDTO);
        books = booksRepository.save(books);
        BooksDTO result = booksMapper.booksToBooksDTO(books);
        return ResponseEntity.created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("books", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /books : Updates an existing books.
     *
     * @param booksDTO the booksDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated booksDTO,
     * or with status 400 (Bad Request) if the booksDTO is not valid,
     * or with status 500 (Internal Server Error) if the booksDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/books",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooksDTO> updateBooks(@RequestBody BooksDTO booksDTO) throws URISyntaxException {
        log.debug("REST request to update Books : {}", booksDTO);
        if (booksDTO.getId() == null) {
            return createBooks(booksDTO);
        }
        Books books = booksMapper.booksDTOToBooks(booksDTO);
        books = booksRepository.save(books);
        BooksDTO result = booksMapper.booksToBooksDTO(books);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("books", booksDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /books : get all the books.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of books in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/books",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BooksDTO>> getAllBooks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Books");
        Page<Books> page = booksRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/books");
        return new ResponseEntity<>(booksMapper.booksToBooksDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /books/:id : get the "id" books.
     *
     * @param id the id of the booksDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the booksDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/books/{id_book}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooksDTO> getBooks(@PathVariable int id_book) {
        log.debug("REST request to get Books : {}", id_book);
        Books books = booksRepository.findByFuckinId(id_book);
        BooksDTO booksDTO = booksMapper.booksToBooksDTO(books);
        return Optional.ofNullable(booksDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /books/:id : delete the "id" books.
     *
     * @param id the id of the booksDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/books/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBooks(@PathVariable String id) {
        log.debug("REST request to delete Books : {}", id);
        booksRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("books", id.toString())).build();
    }

}

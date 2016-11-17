package com.amazing.kundel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.amazing.kundel.domain.Authors;

import com.amazing.kundel.repository.AuthorsRepository;
import com.amazing.kundel.web.rest.util.HeaderUtil;
import com.amazing.kundel.web.rest.util.PaginationUtil;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Authors.
 */
@RestController
@RequestMapping("/api")
public class AuthorsResource {

    private final Logger log = LoggerFactory.getLogger(AuthorsResource.class);

    @Inject
    private AuthorsRepository authorsRepository;

    /**
     * POST  /authors : Create a new authors.
     *
     * @param authors the authors to create
     * @return the ResponseEntity with status 201 (Created) and with body the new authors, or with status 400 (Bad Request) if the authors has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/authors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Authors> createAuthors(@RequestBody Authors authors) throws URISyntaxException {
        log.debug("REST request to save Authors : {}", authors);
        if (authors.getIdAuthor() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("authors", "idexists", "A new authors cannot already have an ID")).body(null);
        }
        Authors result = authorsRepository.save(authors);
        return ResponseEntity.created(new URI("/api/authors/" + result.getIdAuthor()))
            .headers(HeaderUtil.createEntityCreationAlert("authors", result.getIdAuthor().toString()))
            .body(result);
    }

    /**
     * PUT  /authors : Updates an existing authors.
     *
     * @param authors the authors to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated authors,
     * or with status 400 (Bad Request) if the authors is not valid,
     * or with status 500 (Internal Server Error) if the authors couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/authors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Authors> updateAuthors(@RequestBody Authors authors) throws URISyntaxException {
        log.debug("REST request to update Authors : {}", authors);
        if (authors.getId() == null) {
            return createAuthors(authors);
        }
        Authors result = authorsRepository.save(authors);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("authors", authors.getId().toString()))
            .body(result);
    }

    /**
     * GET  /authors : get all the authors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of authors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/authors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Authors>> getAllAuthors(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Authors");
        Page<Authors> page = authorsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/authors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /authors/:id : get the "id" authors.
     *
     * @param id the id of the authors to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the authors, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/authors/{id_author}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Authors> getAuthors(@PathVariable int id_author) {
        log.debug("REST request to get Authors : {}", id_author);
        Authors authors = authorsRepository.findByFuckinId(id_author);
        return Optional.ofNullable(authors)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /authors/:id : delete the "id" authors.
     *
     * @param id the id of the authors to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/authors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAuthors(@PathVariable String id) {
        log.debug("REST request to delete Authors : {}", id);
        authorsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("authors", id.toString())).build();
    }

}

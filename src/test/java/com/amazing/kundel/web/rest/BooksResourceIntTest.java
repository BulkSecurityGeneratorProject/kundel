package com.amazing.kundel.web.rest;

import com.amazing.kundel.KundelApp;

import com.amazing.kundel.domain.Books;
import com.amazing.kundel.repository.BooksRepository;
import com.amazing.kundel.service.dto.BooksDTO;
import com.amazing.kundel.service.mapper.BooksMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BooksResource REST controller.
 *
 * @see BooksResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KundelApp.class)
public class BooksResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final String DEFAULT_ISBN = "AAAAA";
    private static final String UPDATED_ISBN = "BBBBB";

    private static final String DEFAULT_YEAR = "AAAAA";
    private static final String UPDATED_YEAR = "BBBBB";

    private static final String DEFAULT_URL_S = "AAAAA";
    private static final String UPDATED_URL_S = "BBBBB";

    private static final String DEFAULT_URL_M = "AAAAA";
    private static final String UPDATED_URL_M = "BBBBB";

    private static final String DEFAULT_URL_L = "AAAAA";
    private static final String UPDATED_URL_L = "BBBBB";

    private static final Integer DEFAULT_AUTHOR = 1;
    private static final Integer UPDATED_AUTHOR = 2;

    private static final Integer DEFAULT_PUBLISHER = 1;
    private static final Integer UPDATED_PUBLISHER = 2;

    @Inject
    private BooksRepository booksRepository;

    @Inject
    private BooksMapper booksMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBooksMockMvc;

    private Books books;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BooksResource booksResource = new BooksResource();
        ReflectionTestUtils.setField(booksResource, "booksRepository", booksRepository);
        ReflectionTestUtils.setField(booksResource, "booksMapper", booksMapper);
        this.restBooksMockMvc = MockMvcBuilders.standaloneSetup(booksResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Books createEntity() {
        Books books = new Books()
                .title(DEFAULT_TITLE)
                .isbn(DEFAULT_ISBN)
                .year(DEFAULT_YEAR)
                .url_s(DEFAULT_URL_S)
                .url_m(DEFAULT_URL_M)
                .url_l(DEFAULT_URL_L)
                .author(DEFAULT_AUTHOR)
                .publisher(DEFAULT_PUBLISHER);
        return books;
    }

    @Before
    public void initTest() {
        booksRepository.deleteAll();
        books = createEntity();
    }

    @Test
    public void createBooks() throws Exception {
        int databaseSizeBeforeCreate = booksRepository.findAll().size();

        // Create the Books
        BooksDTO booksDTO = booksMapper.booksToBooksDTO(books);

        restBooksMockMvc.perform(post("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booksDTO)))
                .andExpect(status().isCreated());

        // Validate the Books in the database
        List<Books> books = booksRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeCreate + 1);
        Books testBooks = books.get(books.size() - 1);
        assertThat(testBooks.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBooks.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testBooks.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testBooks.getUrl_s()).isEqualTo(DEFAULT_URL_S);
        assertThat(testBooks.getUrl_m()).isEqualTo(DEFAULT_URL_M);
        assertThat(testBooks.getUrl_l()).isEqualTo(DEFAULT_URL_L);
        assertThat(testBooks.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBooks.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
    }

    @Test
    public void getAllBooks() throws Exception {
        // Initialize the database
        booksRepository.save(books);

        // Get all the books
        restBooksMockMvc.perform(get("/api/books?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(books.getId())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
                .andExpect(jsonPath("$.[*].url_s").value(hasItem(DEFAULT_URL_S.toString())))
                .andExpect(jsonPath("$.[*].url_m").value(hasItem(DEFAULT_URL_M.toString())))
                .andExpect(jsonPath("$.[*].url_l").value(hasItem(DEFAULT_URL_L.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
                .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER)));
    }

    @Test
    public void getBooks() throws Exception {
        // Initialize the database
        booksRepository.save(books);

        // Get the books
        restBooksMockMvc.perform(get("/api/books/{id}", books.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(books.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.url_s").value(DEFAULT_URL_S.toString()))
            .andExpect(jsonPath("$.url_m").value(DEFAULT_URL_M.toString()))
            .andExpect(jsonPath("$.url_l").value(DEFAULT_URL_L.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER));
    }

    @Test
    public void getNonExistingBooks() throws Exception {
        // Get the books
        restBooksMockMvc.perform(get("/api/books/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBooks() throws Exception {
        // Initialize the database
        booksRepository.save(books);
        int databaseSizeBeforeUpdate = booksRepository.findAll().size();

        // Update the books
        Books updatedBooks = booksRepository.findOne(books.getId());
        updatedBooks
                .title(UPDATED_TITLE)
                .isbn(UPDATED_ISBN)
                .year(UPDATED_YEAR)
                .url_s(UPDATED_URL_S)
                .url_m(UPDATED_URL_M)
                .url_l(UPDATED_URL_L)
                .author(UPDATED_AUTHOR)
                .publisher(UPDATED_PUBLISHER);
        BooksDTO booksDTO = booksMapper.booksToBooksDTO(updatedBooks);

        restBooksMockMvc.perform(put("/api/books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booksDTO)))
                .andExpect(status().isOk());

        // Validate the Books in the database
        List<Books> books = booksRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeUpdate);
        Books testBooks = books.get(books.size() - 1);
        assertThat(testBooks.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBooks.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBooks.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testBooks.getUrl_s()).isEqualTo(UPDATED_URL_S);
        assertThat(testBooks.getUrl_m()).isEqualTo(UPDATED_URL_M);
        assertThat(testBooks.getUrl_l()).isEqualTo(UPDATED_URL_L);
        assertThat(testBooks.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBooks.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
    }

    @Test
    public void deleteBooks() throws Exception {
        // Initialize the database
        booksRepository.save(books);
        int databaseSizeBeforeDelete = booksRepository.findAll().size();

        // Get the books
        restBooksMockMvc.perform(delete("/api/books/{id}", books.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Books> books = booksRepository.findAll();
        assertThat(books).hasSize(databaseSizeBeforeDelete - 1);
    }
}

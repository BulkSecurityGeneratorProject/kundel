package com.amazing.kundel.web.rest;

import com.amazing.kundel.KundelApp;

import com.amazing.kundel.domain.Authors;
import com.amazing.kundel.repository.AuthorsRepository;

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
 * Test class for the AuthorsResource REST controller.
 *
 * @see AuthorsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KundelApp.class)
public class AuthorsResourceIntTest {

    private static final Integer DEFAULT_ID_AUTHOR = 1;
    private static final Integer UPDATED_ID_AUTHOR = 2;

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private AuthorsRepository authorsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAuthorsMockMvc;

    private Authors authors;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuthorsResource authorsResource = new AuthorsResource();
        ReflectionTestUtils.setField(authorsResource, "authorsRepository", authorsRepository);
        this.restAuthorsMockMvc = MockMvcBuilders.standaloneSetup(authorsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authors createEntity() {
        Authors authors = new Authors()
                .idAuthor(DEFAULT_ID_AUTHOR)
                .name(DEFAULT_NAME);
        return authors;
    }

    @Before
    public void initTest() {
        authorsRepository.deleteAll();
        authors = createEntity();
    }

    @Test
    public void createAuthors() throws Exception {
        int databaseSizeBeforeCreate = authorsRepository.findAll().size();

        // Create the Authors

        restAuthorsMockMvc.perform(post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authors)))
                .andExpect(status().isCreated());

        // Validate the Authors in the database
        List<Authors> authors = authorsRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeCreate + 1);
        Authors testAuthors = authors.get(authors.size() - 1);
        assertThat(testAuthors.getIdAuthor()).isEqualTo(DEFAULT_ID_AUTHOR);
        assertThat(testAuthors.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    public void getAllAuthors() throws Exception {
        // Initialize the database
        authorsRepository.save(authors);

        // Get all the authors
        restAuthorsMockMvc.perform(get("/api/authors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(authors.getId())))
                .andExpect(jsonPath("$.[*].idAuthor").value(hasItem(DEFAULT_ID_AUTHOR)))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    public void getAuthors() throws Exception {
        // Initialize the database
        authorsRepository.save(authors);

        // Get the authors
        restAuthorsMockMvc.perform(get("/api/authors/{id}", authors.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authors.getId()))
            .andExpect(jsonPath("$.idAuthor").value(DEFAULT_ID_AUTHOR))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingAuthors() throws Exception {
        // Get the authors
        restAuthorsMockMvc.perform(get("/api/authors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAuthors() throws Exception {
        // Initialize the database
        authorsRepository.save(authors);
        int databaseSizeBeforeUpdate = authorsRepository.findAll().size();

        // Update the authors
        Authors updatedAuthors = authorsRepository.findOne(authors.getId());
        updatedAuthors
                .idAuthor(UPDATED_ID_AUTHOR)
                .name(UPDATED_NAME);

        restAuthorsMockMvc.perform(put("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAuthors)))
                .andExpect(status().isOk());

        // Validate the Authors in the database
        List<Authors> authors = authorsRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeUpdate);
        Authors testAuthors = authors.get(authors.size() - 1);
        assertThat(testAuthors.getIdAuthor()).isEqualTo(UPDATED_ID_AUTHOR);
        assertThat(testAuthors.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    public void deleteAuthors() throws Exception {
        // Initialize the database
        authorsRepository.save(authors);
        int databaseSizeBeforeDelete = authorsRepository.findAll().size();

        // Get the authors
        restAuthorsMockMvc.perform(delete("/api/authors/{id}", authors.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Authors> authors = authorsRepository.findAll();
        assertThat(authors).hasSize(databaseSizeBeforeDelete - 1);
    }
}

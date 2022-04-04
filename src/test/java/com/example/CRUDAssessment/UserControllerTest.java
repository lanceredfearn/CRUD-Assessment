package com.example.CRUDAssessment;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.is;

import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void init() {
        User testUser1 = new User("john@example.com", "this");
        User testUser2 = new User("paul@example.com", "is");
        User testUser3 = new User("george@example.com", "not");
        User testUser4 = new User("ringo@example.com", "great");
        User testUser5 = new User("dummy@example.com", "password");
        userRepo.save(testUser1);
        userRepo.save(testUser2);
        userRepo.save(testUser3);
        userRepo.save(testUser4);
        userRepo.save(testUser5);
    }

    @Test
    @Transactional
    @Rollback
    void getAllTheUsers() throws Exception {
        this.mvc.perform(get("/users"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("john@example.com")));
    }

    @Test
    @Transactional
    @Rollback
    void insertNewUserUpdatesDatabaseAndReturnsCorrectJson() throws Exception {
        //Setup

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("  {\n" +
                        "    \"email\": \"john@example.com\",\n" +
                        "    \"password\": \"something-secret\"\n" +
                        "  }")
        )
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id", is(6)))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @Rollback
    @Transactional
    void getIndividualUserReturnsEmailAndId() throws Exception {
        this.mvc.perform(get("/users/2"))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("@.email", is("paul@example.com")));
    }

    @Test
    @Rollback
    @Transactional
    void updateUserReturnsUpdatedEmail() throws Exception {
        this.mvc.perform(patch("/users/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"email\": \"john@example.com\"\n" +
                        "}")
        )
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    @Rollback
    @Transactional
    void updateUserReturnsUpdatedEmailAndPassword() throws Exception {
        this.mvc.perform(patch("/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"email\": \"john@example.com\",\n" +
                                "  \"password\": \"1234\"\n" +
                                "}")
                )
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @Rollback
    @Transactional
    void deleteUserByIdReturnsCorrectNumberOfUsers() throws Exception {
        this.mvc.perform(delete("/users/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(4)));
    }

    @Test
    @Rollback
    @Transactional
    void authenticatedUserReturnsCorrectFormat() throws Exception {
        this.mvc.perform(post("/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"email\": \"george@example.com\",\n" +
                        "  \"password\": \"not\"\n" +
                        "}")
        )
                .andExpect(jsonPath("$.authenticated", is(true)))
                .andExpect(jsonPath("$.user.email", is("george@example.com")));

    }
}
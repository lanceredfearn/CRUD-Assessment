package com.example.CRUDAssessment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.*;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.ResultSet;

@Entity
    @Table(name = "users")
    public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String password;
    private String email;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

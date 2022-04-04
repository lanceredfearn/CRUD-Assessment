package com.example.CRUDAssessment;

import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}

package com.example.CRUDAssessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    public final UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public Iterable<User> getAllTheUsers() {
        return userRepo.findAll();
    }

    @PostMapping
    public User insertNewUser(@RequestBody User newUser) {
        return userRepo.save(newUser);

    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepo.findById(id).get();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(value = "id") int id,
            @RequestBody User userDetails) {
        User user = userRepo.findById(id).get();

        if (userDetails.getPassword() == null) {
            user.setEmail(userDetails.getEmail());
            user.setPassword(user.getPassword());
            final User updatedUser = userRepo.save(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            final User updatedUser = userRepo.save(user);
            return ResponseEntity.ok(updatedUser);
        }
    }

    @DeleteMapping("/{id}")
    public HashMap deleteUserByID(@PathVariable int id) {
        userRepo.deleteById(id);
        String count = "count";
        Long userCount = userRepo.count();
        HashMap<String, Long> countMap = new HashMap<>();
        countMap.put(count, userCount);
        return countMap;
    }

    @PostMapping("/authenticate")
    public Object authenticatedUser(@RequestBody Map<String, String> userMap) {

        User savedUser = userRepo.findByEmail(userMap.get("email"));
        System.out.println(savedUser.getPassword());
        HashMap<Object, Object> authMap = new HashMap<>();

        if (savedUser.getPassword().equals(userMap.get("password"))) {
            authMap.put("authenticated", true);
            authMap.put("user", savedUser);
        } else {
            authMap.put("authenticated", false);
        }
        return authMap;
    }
}





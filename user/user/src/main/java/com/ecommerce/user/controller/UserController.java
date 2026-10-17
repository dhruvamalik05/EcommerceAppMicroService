package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userservice;

    @GetMapping
    public List<UserResponse> getallusers() {
        return userservice.fetchuser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getuser(@PathVariable String id) {
        Optional<UserResponse> user = userservice.fetchuser(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.get());
    }

    @PostMapping
    public String createuser(@RequestBody UserRequest userRequest) {

        userservice.adduser(userRequest);
        return "Successfully added";
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateuser (@PathVariable String id, @RequestBody UserRequest userRequest) {
        boolean result = userservice.updateuser(id, userRequest);
        if (result) {
            return ResponseEntity.ok("User added successfully");
        }
        return ResponseEntity.notFound().build();
    }

}

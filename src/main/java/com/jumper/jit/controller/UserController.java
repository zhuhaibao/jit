package com.jumper.jit.controller;

import com.jumper.jit.model.User;
import com.jumper.jit.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Controller("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("add")
    public ResponseEntity<Object> saveUser(@Valid User user, Errors errors){
        if(errors.hasErrors()){
            return new ResponseEntity<>(errors.getAllErrors().toString(),BAD_REQUEST);
        }
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

}

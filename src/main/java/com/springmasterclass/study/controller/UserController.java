package com.springmasterclass.study.controller;

import com.springmasterclass.study.dto.request.UserRq;
import com.springmasterclass.study.dto.response.UserRp;
import com.springmasterclass.study.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserRp>> showListUsers() {
        return new ResponseEntity<>(userService.index(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> creaetNewUser(@RequestBody UserRq userRq) {
        userService.create(userRq);
        return new ResponseEntity<>("Create a new user successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") String id,@RequestBody UserRq userRq){
        userService.update(id, userRq);
        return new ResponseEntity<>("Update a user successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        userService.delete(id);
        return new ResponseEntity<>("Delete a user successfully", HttpStatus.OK);
    }
}

package com.hau.news.controllers;


import com.hau.news.requestbodies.UserRequestBody;
import com.hau.news.requestbodies.UserUpdatedRequestBody;
import com.hau.news.responsebodies.UserResponseBody;
import com.hau.news.responsebodies.UserUpdatedResponseBody;
import com.hau.news.serviceimpls.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserServiceImpl userServiceImpl;
    @Autowired
    public UserController(UserServiceImpl userServiceImpl){
        this.userServiceImpl = userServiceImpl;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<UserResponseBody> createUser(@RequestBody UserRequestBody userRequestBody){
        UserResponseBody newUser = userServiceImpl.createUser(userRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).allow(HttpMethod.GET).body(newUser);
    }
    @GetMapping("/get/{userId}")
    public ResponseEntity<UserResponseBody> getUser(@PathVariable("userId") String userId){
        UserResponseBody user = userServiceImpl.getUserByIdOrThrow(userId);
        return ResponseEntity.ok(user);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update/{userId}")
    public ResponseEntity<UserUpdatedResponseBody> editUserDetailsById(@PathVariable("userId") String userId, @RequestBody UserUpdatedRequestBody userUpdatedRequestBody){
        UserUpdatedResponseBody updatedUser = userServiceImpl.updateUserDetailsById(userId,userUpdatedRequestBody);
        return ResponseEntity.ok(updatedUser);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{userId}")
    public String deleteUserById(@PathVariable("userId")String userId){
        return userServiceImpl.deleteUserById(userId);
    }

}

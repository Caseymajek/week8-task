package com.example.week8blogproject.controller;

import com.example.week8blogproject.model.Users;
import com.example.week8blogproject.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/save-post")
    public ResponseEntity<Users> savePost (@RequestBody Users post) {
        return userService.savePost(post);
    }

    public ResponseEntity<Users> createPost(@RequestBody Users newPost){
        return userService.savePost(newPost);
    }

    @GetMapping("/all-post")
    public ResponseEntity<List<Users>> getAllPost(){
        return userService.getAllPost();
    }


    @GetMapping("/search-post/{title}")
    public ResponseEntity<List<Users>> searchByTitle(@PathVariable String title){
        return userService.searchByTitle(title);
    }

    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id){
        return userService.deletePostById(id);
    }


    @PutMapping("/edit-post/{id}")
    public ResponseEntity<Users> editPostById(@PathVariable Long id, @RequestBody Users postToEdit){
        return userService.editPostById(id, postToEdit);
    }

    @PutMapping("/like/{postId}")
    public ResponseEntity<Users> likePost(@PathVariable Long postId) {
        return userService.likePostById(postId);
    }

    @PutMapping("/unlike/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId) {
        return userService.unlikePostById(postId);
    }
}


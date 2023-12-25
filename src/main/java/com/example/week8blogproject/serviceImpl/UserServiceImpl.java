package com.example.week8blogproject.serviceImpl;

import com.example.week8blogproject.model.Users;
import com.example.week8blogproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Users> savePost(Users newPost) {
        Users post = new Users(newPost.getTitle(), newPost.getImage(), newPost.getDescription(), newPost.isPublished(), newPost.getPostLikes());
        userRepository.save(post);
        return new ResponseEntity<>(post, HttpStatus.CREATED);

    }

    public ResponseEntity<List<Users>> getAllPost() {
        List<Users> allPost = userRepository.findAll();
        return new ResponseEntity<>(allPost, HttpStatus.FOUND);
    }

    public ResponseEntity<List<Users>> searchByTitle(String title) {
        List<Users> allSearchByTitle = userRepository.findByTitleIgnoreCaseStartingWith(title);
        if (allSearchByTitle.isEmpty()) {
            return new ResponseEntity<>(allSearchByTitle, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(allSearchByTitle, HttpStatus.FOUND);

        }
    }

    public ResponseEntity<Void> deletePostById(Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Users> editPostById(Long id, Users postToEdit) {
        Optional<Users> post = userRepository.findById(id);
        if (post.isPresent()) {
            Users post1 = post.get();
            post1.setTitle(postToEdit.getTitle());
            post1.setImage(postToEdit.getImage());
            post1.setDescription(postToEdit.getDescription());
            post1.setPublished(postToEdit.isPublished());
            userRepository.save(post1);
            return new ResponseEntity<>(post1, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    public ResponseEntity<Users> likePostById(Long postId) {
        Users post = userRepository.findById(postId).orElse(null);

        if (post != null){
            post.incrementPost();
            userRepository.save(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> unlikePostById(Long postId) {
        Users post = userRepository.findById(postId).orElse(null);

        if (post!=null && post.getPostLikes() > 0){
            post.decrementPost();
            userRepository.save(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }else{
            Users zeroLikesPost = new Users(post.getTitle(), post.getImage(), post.getDescription(), post.isPublished(), 0L);
            zeroLikesPost.setId(post.getId());
            zeroLikesPost.setPostDate(post.getPostDate());
            return new ResponseEntity<>(zeroLikesPost, HttpStatus.OK);
        }
    }
}



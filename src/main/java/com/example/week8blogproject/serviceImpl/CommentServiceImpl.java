package com.example.week8blogproject.serviceImpl;

import com.example.week8blogproject.model.Comment;
import com.example.week8blogproject.model.Users;
import com.example.week8blogproject.repository.CommentRepository;
import com.example.week8blogproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CommentServiceImpl {

    private final CommentRepository commentRepository;
    private UserRepository userRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository){
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }


    public ResponseEntity<Comment> saveComment(Long userId, Comment newComment) {
        Optional<Users> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Users createdPost = user.get();
            newComment.setPost(createdPost);
            newComment.setContent(newComment.getContent());
            commentRepository.save(newComment);
            return new ResponseEntity<>(newComment, HttpStatus.CREATED);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Comment> editCommentById(Long commentId, Comment commentToEdit) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()){
            Comment comment1 = comment.get();
            comment1.setContent(commentToEdit.getContent());
            commentRepository.save(comment1);
            return new ResponseEntity<>(comment1, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Comment>> findAllCommentById(Long userId) {
        List<Comment> findCommentById = commentRepository.findByPostId(userId);
        if (findCommentById.isEmpty()){
            return new ResponseEntity<>(findCommentById, HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(findCommentById, HttpStatus.FOUND);
        }

    }

    public ResponseEntity<List<Comment>> getAllComment() {
        List<Comment> allComment = commentRepository.findAll();
        return  new ResponseEntity<>(allComment, HttpStatus.FOUND);
    }

    public ResponseEntity<Void> deleteById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()){
            Comment comment1  = comment.get();
            commentRepository.deleteById(comment1.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Comment>> findCommentByContent(String content) {
        List<Comment> findByContent = commentRepository.findByContentIgnoringCaseContaining(content);
        if (findByContent.isEmpty()){
            return new ResponseEntity<>(findByContent, HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(findByContent, HttpStatus.FOUND);
        }
    }

    public ResponseEntity<Comment> likeCommentById(Long id) {
        Optional<Comment> commentToLike = commentRepository.findById(id);
        if (commentToLike.isPresent()){

            Comment comment = commentToLike.get();
            comment.setCommentLikes(comment.getCommentLikes() +1);
            commentRepository.save(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Comment> unlikeCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment!= null && comment.getCommentLikes() > 0){
            comment.decrementComment();
            commentRepository.save(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }else {
            Comment zeroLikes = new Comment(comment.getContent(), comment.getCommentLikes());
            zeroLikes.setId(comment.getId());
            zeroLikes.setCommentDate(comment.getCommentDate());
            return new ResponseEntity<>(zeroLikes, HttpStatus.OK);
        }
    }
}


package com.socialmedia.social_media_backend.service;

import org.springframework.data.domain.Sort;


import com.socialmedia.social_media_backend.exception.ResourceNotFoundException;
import com.socialmedia.social_media_backend.model.Comment;
import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.CommentRepository;
import com.socialmedia.social_media_backend.repository.PostRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Comment createComment(Integer userId, Integer postId, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = Comment.builder()
                .comment_text(content)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .user(user)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    public List<Comment> getCommentsByPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        return commentRepository.findByPost(post);
    }

    public List<Comment> getSortedComments(String dir) {
        Sort sort = Sort.by("timestamp");
        sort = "desc".equalsIgnoreCase(dir) ? sort.descending() : sort.ascending();
        return commentRepository.findAll(sort);
    }

    public void deleteComment(Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found");
        }
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getCommentByUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return commentRepository.findByUser(user);
    }

    public Comment updateComment(Comment updatedComment) {
        Comment existingComment = commentRepository.findById(updatedComment.getCommentID())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));


        existingComment.setComment_text(updatedComment.getComment_text());

        return commentRepository.save(existingComment);
    }
}
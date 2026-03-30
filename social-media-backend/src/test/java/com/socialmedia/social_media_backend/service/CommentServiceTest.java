package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.model.Comment;
import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.CommentRepository;
import com.socialmedia.social_media_backend.repository.PostRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserID(1);

        post = new Post();
        post.setPostID(10);

        comment = Comment.builder()
                .commentID(100)
                .comment_text("Test comment")
                .user(user)
                .post(post)
                .build();
    }


    @Test
    void testCreateComment() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(10)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.createComment(1, 10, "Test comment");

        assertNotNull(result);
        assertEquals("Test comment", result.getComment_text());
    }

    @Test
    void testGetAllComments() {
        when(commentRepository.findAll()).thenReturn(List.of(comment));

        List<Comment> result = commentService.getAllComments();

        assertEquals(1, result.size());
    }


    @Test
    void testGetCommentById() {
        when(commentRepository.findById(100)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(100);

        assertEquals(100, result.getCommentID());
    }

    @Test
    void testDeleteComment() {
        when(commentRepository.existsById(100)).thenReturn(true);

        commentService.deleteComment(100);

        verify(commentRepository, times(1)).deleteById(100);
    }

    @Test
    void testUpdateComment() {
        Comment updated = new Comment();
        updated.setCommentID(100);
        updated.setComment_text("Updated");

        when(commentRepository.findById(100)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.updateComment(updated);

        assertEquals("Updated", result.getComment_text());
    }

    @Test
    void testGetCommentsByPost() {
        when(postRepository.findById(10)).thenReturn(Optional.of(post));
        when(commentRepository.findByPost(post)).thenReturn(List.of(comment));

        List<Comment> result = commentService.getCommentsByPost(10);

        assertEquals(1, result.size());
    }

    @Test
    void testGetCommentsByUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(commentRepository.findByUser(user)).thenReturn(List.of(comment));

        List<Comment> result = commentService.getCommentByUser(1);

        assertEquals(1, result.size());
    }
}
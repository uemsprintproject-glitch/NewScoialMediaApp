package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.PostRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserID(1);

        post = Post.builder()
                .postID(100)
                .content("Test post")
                .user(user)
                .build();
    }

    @Test
    void getPostById_success() {
        when(postRepository.findById(100)).thenReturn(Optional.of(post));

        Post result = postService.getPostById(100);

        assertEquals(100, result.getPostID());
    }

    @Test
    void getPostById_notFound() {
        when(postRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            postService.getPostById(100);
        });
    }

    @Test
    void createPost_success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postService.createPost(1, "Test post");

        assertNotNull(result);
        assertEquals("Test post", result.getContent());
    }

    @Test
    void createPost_userNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            postService.createPost(1, "Test");
        });
    }

    @Test
    void getAllPosts_success() {
        when(postRepository.findAllByOrderByTimestampDesc())
                .thenReturn(List.of(post));

        List<Post> result = postService.getAllPosts();

        assertEquals(1, result.size());
    }

    @Test
    void getPostsByUser_success() {
        user.setPosts(List.of(post));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        List<Post> result = postService.getPostsByUser(1);

        assertEquals(1, result.size());
    }

    @Test
    void deletePost_success() {
        postService.deletePost(100);

        verify(postRepository, times(1)).deleteById(100);
    }

    @Test
    void updatePost_success() {
        Post updated = new Post();
        updated.setPostID(100);
        updated.setContent("Updated");

        when(postRepository.findById(100)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postService.updatePost(updated);

        assertEquals("Updated", result.getContent());
    }

    @Test
    void updatePost_notFound() {
        Post updated = new Post();
        updated.setPostID(100);

        when(postRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            postService.updatePost(updated);
        });
    }
}
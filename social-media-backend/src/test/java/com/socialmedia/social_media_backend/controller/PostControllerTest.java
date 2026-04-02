package com.socialmedia.social_media_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.social_media_backend.exception.GlobalExceptionHandler;
import com.socialmedia.social_media_backend.exception.ResourceNotFoundException;
import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PostService service;

    @InjectMocks
    private PostController controller;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        user = User.builder()
                .userID(1)
                .username("anishka")
                .email("anishka@gmail.com")
                .password("123")
                .build();

        post = Post.builder()
                .postID(100)
                .content("Test post")
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .user(user)
                .build();
    }

    @Test
    void testGetAllPosts() throws Exception {
        when(service.getAllPosts()).thenReturn(List.of(post));

        mockMvc.perform(get("/member/posts/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].postID").value(100))
                .andExpect(jsonPath("$[0].content").value("Test post"));
    }

    @Test
    void testGetPostById() throws Exception {
        when(service.getPostById(100)).thenReturn(post);

        mockMvc.perform(get("/member/posts/by-id").param("postId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postID").value(100))
                .andExpect(jsonPath("$.content").value("Test post"));
    }

    @Test
    void testGetPostById_notFound() throws Exception {

        when(service.getPostById(999))
                .thenThrow(new ResourceNotFoundException("Post not found with id: 999"));

        mockMvc.perform(get("/member/posts/by-id").param("postId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Post not found with id: 999"));
    }

    @Test
    void testGetPostsByUser() throws Exception {
        when(service.getPostsByUser(1)).thenReturn(List.of(post));

        mockMvc.perform(get("/member/posts/by-user").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].content").value("Test post"));
    }

    @Test
    void testGetPostsByUser_notFound() throws Exception {

        when(service.getPostsByUser(999))
                .thenThrow(new ResourceNotFoundException("User not found with id: 999"));

        mockMvc.perform(get("/member/posts/by-user").param("userId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 999"));
    }

    @Test
    void testCreatePost() throws Exception {
        when(service.createPost(eq(1), eq("Test post"))).thenReturn(post);

        mockMvc.perform(post("/member/posts/create")
                        .param("userId", "1")
                        .param("content", "Test post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postID").value(100))
                .andExpect(jsonPath("$.content").value("Test post"));
    }

    @Test
    void testCreatePost_userNotFound() throws Exception {

        when(service.createPost(eq(999), any()))
                .thenThrow(new ResourceNotFoundException("User not found with id: 999"));

        mockMvc.perform(post("/member/posts/create")
                        .param("userId", "999")
                        .param("content", "Test post"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 999"));
    }

    @Test
    void testUpdatePost() throws Exception {
        Post updatedPost = Post.builder()
                .postID(100)
                .content("Updated content")
                .user(user)
                .build();

        when(service.updatePost(any(Post.class))).thenReturn(updatedPost);

        mockMvc.perform(post("/member/posts/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

    @Test
    void testUpdatePost_notFound() throws Exception {

        Post missingPost = Post.builder()
                .postID(999)
                .content("Doesn't matter")
                .build();

        when(service.updatePost(any(Post.class)))
                .thenThrow(new ResourceNotFoundException("Post not found with id: 999"));

        mockMvc.perform(post("/member/posts/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missingPost)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Post not found with id: 999"));
    }

    @Test
    void testDeletePost() throws Exception {
        doNothing().when(service).deletePost(100);

        mockMvc.perform(post("/member/posts/delete").param("postId", "100"))
                .andExpect(status().isOk());

        verify(service, times(1)).deletePost(100);
    }
}
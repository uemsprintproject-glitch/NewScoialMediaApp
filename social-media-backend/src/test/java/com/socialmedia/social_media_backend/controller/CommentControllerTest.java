package com.socialmedia.social_media_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmedia.social_media_backend.exception.GlobalExceptionHandler;
import com.socialmedia.social_media_backend.exception.ResourceNotFoundException;
import com.socialmedia.social_media_backend.model.Comment;
import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest {

        private MockMvc mockMvc;
        private ObjectMapper objectMapper;

        @Mock
        private CommentService service;

        @InjectMocks
        private CommentController controller;

        private User user;
        private Post post;
        private Comment comment;

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
                                .postID(10)
                                .content("Test post")
                                .user(user)
                                .build();

                comment = Comment.builder()
                                .commentID(100)
                                .comment_text("Test comment")
                                .user(user)
                                .post(post)
                                .build();
        }

        @Test
        void testGetAllComments() throws Exception {
                when(service.getAllComments()).thenReturn(List.of(comment));

                mockMvc.perform(get("/member/comments/all"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].commentID").value(100))
                                .andExpect(jsonPath("$[0].comment_text").value("Test comment"));
        }

        @Test
        void testGetCommentById() throws Exception {
                when(service.getCommentById(100)).thenReturn(comment);

                mockMvc.perform(get("/member/comments/by-id").param("id", "100"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.commentID").value(100))
                                .andExpect(jsonPath("$.comment_text").value("Test comment"));
        }

        @Test
        void testGetCommentById_notFound() throws Exception {
                when(service.getCommentById(999))
                                .thenThrow(new ResourceNotFoundException("Comment not found with id: 999"));

                mockMvc.perform(get("/member/comments/by-id").param("id", "999"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("Comment not found with id: 999"));
        }

        @Test
        void testGetCommentsByUser() throws Exception {
                when(service.getCommentByUser(1)).thenReturn(List.of(comment));

                mockMvc.perform(get("/member/comments/by-user").param("userId", "1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].comment_text").value("Test comment"));
        }

        @Test
        void testGetCommentsByUser_notFound() throws Exception {
                when(service.getCommentByUser(999))
                                .thenThrow(new ResourceNotFoundException("User not found with id: 999"));

                mockMvc.perform(get("/member/comments/by-user").param("userId", "999"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("User not found with id: 999"));
        }

        @Test
        void testCreateComment() throws Exception {
                when(service.createComment(
                                eq(user.getUserID()),
                                eq(post.getPostID()),
                                eq("Test comment"))).thenReturn(comment);

                mockMvc.perform(post("/member/comments/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(comment)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.commentID").value(100))
                                .andExpect(jsonPath("$.comment_text").value("Test comment"));
        }

        @Test
        void testCreateComment_userNotFound() throws Exception {
                when(service.createComment(anyInt(), anyInt(), any()))
                                .thenThrow(new ResourceNotFoundException("User not found with id: 1"));

                mockMvc.perform(post("/member/comments/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(comment)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("User not found with id: 1"));
        }

        @Test
        void testCreateComment_postNotFound() throws Exception {
                when(service.createComment(anyInt(), anyInt(), any()))
                                .thenThrow(new ResourceNotFoundException("Post not found with id: 10"));

                mockMvc.perform(post("/member/comments/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(comment)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("Post not found with id: 10"));
        }

        @Test
        void testUpdateComment() throws Exception {
                Comment updatedComment = Comment.builder()
                                .commentID(100)
                                .comment_text("Updated comment")
                                .user(user)
                                .post(post)
                                .build();

                when(service.updateComment(any(Comment.class))).thenReturn(updatedComment);

                mockMvc.perform(put("/member/comments/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedComment)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.commentID").value(100))
                                .andExpect(jsonPath("$.comment_text").value("Updated comment"));
        }

        @Test
        void testUpdateComment_notFound() throws Exception {
                Comment missingComment = Comment.builder()
                                .commentID(999)
                                .comment_text("Doesn't matter")
                                .user(user)
                                .post(post)
                                .build();

                when(service.updateComment(any(Comment.class)))
                                .thenThrow(new ResourceNotFoundException("Comment not found with id: 999"));

                mockMvc.perform(put("/member/comments/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(missingComment)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("Comment not found with id: 999"));
        }

        @Test
        void testDeleteComment() throws Exception {
                doNothing().when(service).deleteComment(100);

                mockMvc.perform(post("/member/comments/delete").param("id", "100"))
                                .andExpect(status().isOk());

                verify(service, times(1)).deleteComment(100);
        }

        @Test
        void testGetCommentsByPost() throws Exception {
                when(service.getCommentsByPost(10)).thenReturn(List.of(comment));

                mockMvc.perform(get("/member/comments/by-post").param("postId", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].comment_text").value("Test comment"));
        }

        @Test
        void testGetCommentsByPost_notFound() throws Exception {
                when(service.getCommentsByPost(999))
                                .thenThrow(new ResourceNotFoundException("Post not found with id: 999"));

                mockMvc.perform(get("/member/comments/by-post").param("postId", "999"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("Post not found with id: 999"));
        }
}
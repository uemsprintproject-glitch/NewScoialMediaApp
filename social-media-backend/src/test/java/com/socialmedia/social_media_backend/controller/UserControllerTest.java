package com.socialmedia.social_media_backend.controller;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.socialmedia.social_media_backend.model.Comment;
import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

public class UserControllerTest {
    private MockMvc mockMvc;
    
    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllUsers() throws Exception{
        when(service.getAllUsers()).thenReturn(List.of());
        mockMvc.perform(get("/member/users/all")).andExpect(status().isOk());
    }

    @Test
    public void testGetUserById() throws Exception{
        when(service.getUserById(1)).thenReturn(new User(1, "Anishka", "anishka@gmail.com", "123", null, null, null));
        mockMvc.perform(get("/member/users/by-id?id=1")).andExpect(status().isOk());
    }

    @Test
    public void testGetUserByUsername() throws Exception{
        when(service.getUserByUsername("anishka")).thenReturn(List.of(new User(1, "Anishka", "anishka@gmail.com", "123", null, null, null)));
        mockMvc.perform(get("/member/users/username?username=anishka")).andExpect(status().isOk());
    }

    @Test
    public void testGetUserByEmail() throws Exception{
        when(service.getUserByEmail("anishka@gmail.com")).thenReturn(new User(1, "Anishka", "anishka@gmail.com", "123", null, null, null));
        mockMvc.perform(get("/member/users/email?email=anishka@gmail.com")).andExpect(status().isOk());
    }

    @Test
    public void testGetUserByPostID() throws Exception{
        User user = User.builder().userID(1).username("anishka").email("anishka@gmail.com").password("123").build();
        Post post = Post.builder().postID(101).content("hello").user(user).build();
        user.setPosts(List.of(post));
        when(service.getUserByPostId(post.getPostID())).thenReturn(user);
        mockMvc.perform(get("/member/users/posts?id=101")).andExpect(status().isOk());
    }

    @Test
    public void testGetUserByCommentID() throws Exception{
        User user = User.builder().userID(1).username("anishka").email("anishka@gmail.com").password("123").build();
        Comment comment = Comment.builder().commentID(100001).comment_text("Nice Work").user(user).build();
        user.setComments(List.of(comment));
        when(service.getUserByComment(100001)).thenReturn(user);
        mockMvc.perform(get("/member/users/comment?id=100001")).andExpect(status().isOk());
    }
}

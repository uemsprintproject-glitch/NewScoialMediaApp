package com.socialmedia.social_media_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.socialmedia.social_media_backend.model.Like;
import com.socialmedia.social_media_backend.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

public class LikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LikeService service;

    @InjectMocks
    private LikeController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllLikes() throws Exception {
        when(service.getAllLikes()).thenReturn(List.of());

        mockMvc.perform(get("/member/likes/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLikeById() throws Exception {
        when(service.getLikeById(1)).thenReturn(new Like());

        mockMvc.perform(get("/member/likes/by-id?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLikesByUser() throws Exception {
        when(service.getLikesByUser(1)).thenReturn(List.of(new Like()));

        mockMvc.perform(get("/member/likes/by-user?userID=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLikesByPost() throws Exception {
        when(service.getLikesByPost(1)).thenReturn(List.of(new Like()));

        mockMvc.perform(get("/member/likes/by-post?postID=1"))
                .andExpect(status().isOk());
    }
}
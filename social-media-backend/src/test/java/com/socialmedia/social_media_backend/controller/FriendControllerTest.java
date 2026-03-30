package com.socialmedia.social_media_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.socialmedia.social_media_backend.model.Friend;
import com.socialmedia.social_media_backend.service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

public class FriendControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FriendService service;

    @InjectMocks
    private FriendController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllFriends() throws Exception {
        when(service.getAllFriends()).thenReturn(List.of());

        mockMvc.perform(get("/member/friends/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFriendById() throws Exception {
        when(service.getFriendById(1)).thenReturn(new Friend());

        mockMvc.perform(get("/member/friends/by-id?friendshipId=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPendingRequests() throws Exception {
        when(service.getPendingRequests(1)).thenReturn(List.of(new Friend()));

        mockMvc.perform(get("/member/friends/requests/pending?userId=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSentRequests() throws Exception {
        when(service.getSentRequests(1)).thenReturn(List.of(new Friend()));

        mockMvc.perform(get("/member/friends/requests/sent?userId=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckFriendship() throws Exception {
        when(service.areFriends(1, 2)).thenReturn(true);

        mockMvc.perform(get("/member/friends/check?userId1=1&userId2=2"))
                .andExpect(status().isOk());
    }
}
package com.socialmedia.social_media_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.socialmedia.social_media_backend.model.Notification;
import com.socialmedia.social_media_backend.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

public class NotificationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NotificationService service;

    @InjectMocks
    private NotificationController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        when(service.getAllNotifications()).thenReturn(List.of());

        mockMvc.perform(get("/member/notifications/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNotificationById() throws Exception {
        when(service.getNotificationById(1)).thenReturn(new Notification());

        mockMvc.perform(get("/member/notifications/by-id?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNotificationsByUser() throws Exception {
        when(service.getNotificationsByUser(1)).thenReturn(List.of(new Notification()));

        mockMvc.perform(get("/member/notifications/by-user?userID=1"))
                .andExpect(status().isOk());
    }
}
package com.socialmedia.social_media_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.socialmedia.social_media_backend.model.Message;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.MessageService;
import com.socialmedia.social_media_backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

public class MessageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessageController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllMessages() throws Exception {
        when(messageService.getAllMessages()).thenReturn(List.of());

        mockMvc.perform(get("/member/messages/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMessageById() throws Exception {
        Message msg = Message.builder()
                .messageID(1)
                .message_text("Hello")
                .build();

        when(messageService.getMessageByIdSafe(1)).thenReturn(msg);

        mockMvc.perform(get("/member/messages/by-id?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMessagesBySender() throws Exception {

        User sender = User.builder()
                .userID(1)
                .username("anishka")
                .email("anishka@gmail.com")
                .build();

        Message msg = Message.builder()
                .messageID(1)
                .message_text("Hi")
                .sender(sender)
                .build();

        when(userService.getUserById(1)).thenReturn(sender);
        when(messageService.getMessagesBySender(sender))
                .thenReturn(List.of(msg));

        mockMvc.perform(get("/member/messages/by-sender?senderId=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMessagesByReceiver() throws Exception {

        User receiver = User.builder()
                .userID(2)
                .username("user2")
                .email("user2@gmail.com")
                .build();

        Message msg = Message.builder()
                .messageID(2)
                .message_text("Hello")
                .receiver(receiver)
                .build();

        when(userService.getUserById(2)).thenReturn(receiver);
        when(messageService.getMessagesByReceiver(receiver))
                .thenReturn(List.of(msg));

        mockMvc.perform(get("/member/messages/by-receiver?receiverId=2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetConversation() throws Exception {

        User u1 = User.builder().userID(1).build();
        User u2 = User.builder().userID(2).build();

        Message msg = Message.builder()
                .messageID(1)
                .message_text("Hey")
                .sender(u1)
                .receiver(u2)
                .build();

        when(userService.getUserById(1)).thenReturn(u1);
        when(userService.getUserById(2)).thenReturn(u2);
        when(messageService.getConversation(u1, u2))
                .thenReturn(List.of(msg));

        mockMvc.perform(get("/member/messages/conversation?user1=1&user2=2"))
                .andExpect(status().isOk());
    }
}
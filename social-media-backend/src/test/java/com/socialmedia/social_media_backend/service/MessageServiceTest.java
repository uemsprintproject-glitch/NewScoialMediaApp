package com.socialmedia.social_media_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.socialmedia.social_media_backend.model.Message;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.MessageRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMessages() {

        when(messageRepository.findAll()).thenReturn(List.of());

        List<Message> result = messageService.getAllMessages();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetMessageById() {

        Message msg = Message.builder()
                .messageID(1)
                .message_text("Hello")
                .build();

        when(messageRepository.findById(1)).thenReturn(Optional.of(msg));

        Message result = messageService.getMessageByIdSafe(1);

        assertNotNull(result);
        assertEquals("Hello", result.getMessage_text());
    }

    @Test
    public void testSendMessage() {

        User sender = User.builder().userID(1).build();
        User receiver = User.builder().userID(2).build();

        Message msg = Message.builder()
                .message_text("Hi")
                .sender(sender)
                .receiver(receiver)
                .build();

        when(messageRepository.save(msg)).thenReturn(msg);

        Message result = messageService.sendMessage(msg);

        assertNotNull(result);
        assertEquals("Hi", result.getMessage_text());
    }

    @Test
    public void testUpdateMessage() {

        Message existing = Message.builder()
                .messageID(1)
                .message_text("Old")
                .build();

        Message updated = Message.builder()
                .messageID(1)
                .message_text("New")
                .build();

        when(messageRepository.findById(1)).thenReturn(Optional.of(existing));
        when(messageRepository.save(existing)).thenReturn(existing);

        Message result = messageService.updateMessage(updated);

        assertEquals("New", result.getMessage_text());
    }

    @Test
    public void testUpdateMessage_NotFound() {

        Message updated = Message.builder()
                .messageID(99)
                .message_text("New")
                .build();

        when(messageRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            messageService.updateMessage(updated);
        });
    }

    @Test
    public void testDeleteMessage() {

        when(messageRepository.existsById(1)).thenReturn(true);

        boolean result = messageService.deleteMessageSafe(1);

        assertTrue(result);
        verify(messageRepository).deleteById(1);
    }

    @Test
    public void testDeleteMessage_NotFound() {

        when(messageRepository.existsById(1)).thenReturn(false);

        boolean result = messageService.deleteMessageSafe(1);

        assertFalse(result);
    }

    @Test
    public void testGetMessagesBySender() {

        User sender = User.builder().userID(1).build();

        when(messageRepository.findBySender_UserID(1))
                .thenReturn(List.of());

        List<Message> result = messageService.getMessagesBySender(sender);

        assertNotNull(result);
    }

    @Test
    public void testGetMessagesByReceiver() {

        User receiver = User.builder().userID(2).build();

        when(messageRepository.findByReceiver_UserID(2))
                .thenReturn(List.of());

        List<Message> result = messageService.getMessagesByReceiver(receiver);

        assertNotNull(result);
    }

    @Test
    public void testGetConversation() {

        User u1 = User.builder().userID(1).build();
        User u2 = User.builder().userID(2).build();

        when(messageRepository
                .findBySender_UserIDAndReceiver_UserIDOrSender_UserIDAndReceiver_UserID(
                        1, 2, 2, 1))
                .thenReturn(List.of());

        List<Message> result = messageService.getConversation(u1, u2);

        assertNotNull(result);
    }
}
package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Message;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.MessageService;
import com.socialmedia.social_media_backend.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:8080")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    // GET all messages
    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // GET message by ID
    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable Integer id) {
        return messageService.getMessageByIdSafe(id);
    }

    // SEND message
    @PostMapping("/send")
    public Message sendMessage(@RequestParam Integer senderID,
            @RequestParam Integer receiverID,
            @RequestParam String message_text) {

        User sender = userService.getUserById(senderID);
        User receiver = userService.getUserById(receiverID);

        if (sender == null || receiver == null) {
            throw new RuntimeException("Invalid sender or receiver ID");
        }

        Message message = Message.builder()
                .message_text(message_text)
                .sender(sender)
                .receiver(receiver)
                .build();

        return messageService.sendMessage(message);
    }

    // DELETE message
    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable Integer id) {

        boolean deleted = messageService.deleteMessageSafe(id);

        if (!deleted) {
            throw new RuntimeException("Message not found with ID: " + id);
        }

        return "Message deleted successfully";
    }

    // GET messages by sender
    @GetMapping("/sender/{senderId}")
    public List<Message> getMessagesBySender(@PathVariable Integer senderId) {
        User sender = userService.getUserById(senderId);

        if (sender == null) {
            throw new RuntimeException("Sender not found");
        }

        return messageService.getMessagesBySender(sender);
    }

    // GET messages by receiver
    @GetMapping("/receiver/{receiverId}")
    public List<Message> getMessagesByReceiver(@PathVariable Integer receiverId) {
        User receiver = userService.getUserById(receiverId);

        if (receiver == null) {
            throw new RuntimeException("Receiver not found");
        }

        return messageService.getMessagesByReceiver(receiver);
    }

    @GetMapping("/conversation")
    public List<Message> getConversation(@RequestParam Integer user1,
            @RequestParam Integer user2) {

        User u1 = userService.getUserById(user1);
        User u2 = userService.getUserById(user2);

        if (u1 == null || u2 == null) {
            throw new RuntimeException("Invalid user IDs");
        }

        return messageService.getConversation(u1, u2);
    }
}
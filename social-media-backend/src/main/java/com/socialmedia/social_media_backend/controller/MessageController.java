package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Message;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.MessageService;
import com.socialmedia.social_media_backend.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:8080")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/{id}")
    public Message getMessageById(@PathVariable Integer id) {
        Message message = messageService.getMessageByIdSafe(id);
        if (message == null)
            throw new RuntimeException("Message not found");
        return message;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message sendMessage(@RequestBody Message message) {

        if (message.getSender() == null || message.getReceiver() == null) {
            throw new RuntimeException("Sender and Receiver required");
        }

        return messageService.sendMessage(message);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable Integer id, @RequestBody Message message) {

        message.setMessageID(id);

        return messageService.updateMessage(message);
    }

    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable Integer id) {
        boolean deleted = messageService.deleteMessageSafe(id);
        if (!deleted)
            throw new RuntimeException("Message not found with ID: " + id);

        return "Message deleted successfully";
    }

    @GetMapping("/sender/{senderId}")
    public List<Message> getMessagesBySender(@PathVariable Integer senderId) {
        User sender = userService.getUserById(senderId);
        if (sender == null)
            throw new RuntimeException("Sender not found");

        return messageService.getMessagesBySender(sender);
    }

    @GetMapping("/receiver/{receiverId}")
    public List<Message> getMessagesByReceiver(@PathVariable Integer receiverId) {
        User receiver = userService.getUserById(receiverId);
        if (receiver == null)
            throw new RuntimeException("Receiver not found");

        return messageService.getMessagesByReceiver(receiver);
    }

    @GetMapping("/conversation")
    public List<Message> getConversation(@RequestParam Integer user1,
            @RequestParam Integer user2) {

        User u1 = userService.getUserById(user1);
        User u2 = userService.getUserById(user2);

        if (u1 == null || u2 == null)
            throw new RuntimeException("Invalid user IDs");

        return messageService.getConversation(u1, u2);
    }
}
package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Message;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.MessageService;
import com.socialmedia.social_media_backend.service.UserService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/messages")
@CrossOrigin(origins = "http://localhost:8080")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<Message> getAllMessages() {
        System.out.println("Hello");
        return messageService.getAllMessages();
    }

    @GetMapping("/by-id")
    public Message getMessageById(@RequestParam Integer id) {
        return messageService.getMessageByIdSafe(id);
    }

    // Backward-compatible lookup for clients that call /member/messages/{id}
    @GetMapping("/{id}")
    public Message getMessageByIdPath(@PathVariable Integer id) {
        return messageService.getMessageByIdSafe(id);
    }

    // ✅ correct endpoint
    @PostMapping("/create")
    public Message sendMessage(@RequestBody Message message) {

        if (message.getSender() == null || message.getReceiver() == null) {
            throw new RuntimeException("Sender and Receiver required");
        }

        return messageService.sendMessage(message);
    }

    // ✅ PUT instead of POST
    @PutMapping("/update")
    public Message updateMessage(@RequestBody Message message) {
        return messageService.updateMessage(message);
    }

    // ✅ DELETE instead of POST
    @DeleteMapping("/delete")
    public String deleteMessage(@RequestParam Integer id) {
        boolean deleted = messageService.deleteMessageSafe(id);
        return deleted ? "Message deleted" : "Message not found";
    }

    @GetMapping("/by-sender")
    public List<Message> getMessagesBySender(@RequestParam Integer senderId) {

        User sender = userService.getUserById(senderId);

        if (sender == null) {
            throw new RuntimeException("Sender not found");
        }

        return messageService.getMessagesBySender(sender);
    }

    @GetMapping("/by-receiver")
    public List<Message> getMessagesByReceiver(@RequestParam Integer receiverId) {

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
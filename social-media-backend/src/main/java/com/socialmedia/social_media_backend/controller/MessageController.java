package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Message;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.MessageService;
import com.socialmedia.social_media_backend.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/api/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/api/messages/{id}")
    public Message getMessageById(@PathVariable Integer id) {
        return messageService.getMessageByIdSafe(id);
    }

    @PostMapping("/api/messages/send")
    public Message sendMessage(@RequestParam Integer senderID,
            @RequestParam Integer receiverID,
            @RequestParam String message_text) {
        User sender = userService.getUserById(senderID);
        User receiver = userService.getUserById(receiverID);
        if (sender == null || receiver == null)
            throw new RuntimeException("Invalid sender or receiver ID");
        Message message = Message.builder()
                .message_text(message_text).sender(sender).receiver(receiver).build();
        return messageService.sendMessage(message);
    }

    @DeleteMapping("/api/messages/{id}")
    public String deleteMessage(@PathVariable Integer id) {
        boolean deleted = messageService.deleteMessageSafe(id);
        if (!deleted)
            throw new RuntimeException("Message not found with ID: " + id);
        return "Message deleted successfully";
    }

    @GetMapping("/api/messages/sender/{senderId}")
    public List<Message> getMessagesBySender(@PathVariable Integer senderId) {
        User sender = userService.getUserById(senderId);
        if (sender == null)
            throw new RuntimeException("Sender not found");
        return messageService.getMessagesBySender(sender);
    }

    @GetMapping("/api/messages/receiver/{receiverId}")
    public List<Message> getMessagesByReceiver(@PathVariable Integer receiverId) {
        User receiver = userService.getUserById(receiverId);
        if (receiver == null)
            throw new RuntimeException("Receiver not found");
        return messageService.getMessagesByReceiver(receiver);
    }

    @GetMapping("/api/messages/conversation")
    public List<Message> getConversation(@RequestParam Integer user1,
            @RequestParam Integer user2) {
        User u1 = userService.getUserById(user1);
        User u2 = userService.getUserById(user2);
        if (u1 == null || u2 == null)
            throw new RuntimeException("Invalid user IDs");
        return messageService.getConversation(u1, u2);
    }

    // ---------------------------------------------------------------------
    @GetMapping("/messages")
    public ModelAndView messagesHome() {
        return new ModelAndView("messages");
    }

    @GetMapping("/messages/all")
    public ModelAndView getAllMessagesView() {
        ModelAndView mav = new ModelAndView("message-result");
        mav.addObject("data", messageService.getAllMessages());
        mav.addObject("title", "All Messages");
        return mav;
    }

    @GetMapping("/messages/by-id")
    public ModelAndView getMessageByIdView(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("message-result");
        Message msg = messageService.getMessageByIdSafe(id);
        mav.addObject("data", msg != null ? List.of(msg) : List.of());
        mav.addObject("title", "Message #" + id);
        return mav;
    }

    @PostMapping("/messages/send")
    public ModelAndView sendMessageView(@RequestParam Integer senderID,
            @RequestParam Integer receiverID,
            @RequestParam String message_text) {
        ModelAndView mav = new ModelAndView("message-result");
        User sender = userService.getUserById(senderID);
        User receiver = userService.getUserById(receiverID);
        if (sender == null || receiver == null) {
            mav.addObject("error", "Invalid sender or receiver ID");
            mav.addObject("data", List.of());
            return mav;
        }
        Message message = Message.builder()
                .message_text(message_text).sender(sender).receiver(receiver).build();
        messageService.sendMessage(message);
        mav.addObject("data", messageService.getAllMessages());
        mav.addObject("title", "Message Sent! All Messages");
        return mav;
    }

    @PostMapping("/messages/delete")
    public ModelAndView deleteMessageView(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("message-result");
        boolean deleted = messageService.deleteMessageSafe(id);
        mav.addObject("data", messageService.getAllMessages());
        mav.addObject("title", deleted
                ? "Deleted #" + id + " — Remaining Messages"
                : "ID not found — All Messages");
        return mav;
    }

    @GetMapping("/messages/by-sender")
    public ModelAndView getBySenderView(@RequestParam Integer senderId) {
        ModelAndView mav = new ModelAndView("message-result");
        User sender = userService.getUserById(senderId);
        mav.addObject("data", sender != null
                ? messageService.getMessagesBySender(sender)
                : List.of());
        mav.addObject("title", "Messages from Sender #" + senderId);
        return mav;
    }

    @GetMapping("/messages/by-receiver")
    public ModelAndView getByReceiverView(@RequestParam Integer receiverId) {
        ModelAndView mav = new ModelAndView("message-result");
        User receiver = userService.getUserById(receiverId);
        mav.addObject("data", receiver != null
                ? messageService.getMessagesByReceiver(receiver)
                : List.of());
        mav.addObject("title", "Messages to Receiver #" + receiverId);
        return mav;
    }

    @GetMapping("/messages/conversation")
    public ModelAndView getConversationView(@RequestParam Integer user1,
            @RequestParam Integer user2) {
        ModelAndView mav = new ModelAndView("message-result");
        User u1 = userService.getUserById(user1);
        User u2 = userService.getUserById(user2);
        mav.addObject("data", (u1 != null && u2 != null)
                ? messageService.getConversation(u1, u2)
                : List.of());
        mav.addObject("title", "Conversation: User #" + user1 + " ↔ #" + user2);
        return mav;
    }
}

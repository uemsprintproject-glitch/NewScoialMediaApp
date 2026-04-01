package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Message;
import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.MessageClientService;
import com.socialmedia.social_media_frontend.util.PaginationUtils;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/member/messages")
public class MessageController {

    private final MessageClientService service;

    public MessageController(MessageClientService service) {
        this.service = service;
    }

    @GetMapping
    public String page() {
        return "messages/messages";
    }

    @GetMapping("/all")
    public String getAllMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getAllMessages(),
                page,
                size,
                "/member/messages/all",
                "messages/result",
                model,
                Map.of());
    }

    @GetMapping("/by-id")
    public String getMessageById(
            @RequestParam int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Message message = service.getMessageById(id);
        if (message == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found with ID: " + id);
        }
        return PaginationUtils.renderPaginatedResult(
                List.of(message),
                page,
                size,
                "/member/messages/by-id",
                "messages/result",
                model,
                Map.of("id", id));
    }

    @GetMapping("/by-id-form")
    public String getByIdForm() {
        return "messages/get-by-id";
    }

    @GetMapping("/sender-form")
    public String getBySenderForm() {
        return "messages/get-by-sender";
    }

    @GetMapping("/receiver-form")
    public String getByReceiverForm() {
        return "messages/get-by-receiver";
    }

    @GetMapping("/conversation-form")
    public String getConversationForm() {
        return "messages/get-conversation";
    }

    @GetMapping("/send-form")
    public String getSendForm() {
        return "messages/send-message";
    }

    @GetMapping("/update-form")
    public String getUpdateForm() {
        return "messages/update-message";
    }

    @GetMapping("/delete-form")
    public String getDeleteForm() {
        return "messages/delete-message";
    }

    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message) {

        // ✅ ensure nested objects exist
        if (message.getSender() == null)
            message.setSender(new User());
        if (message.getReceiver() == null)
            message.setReceiver(new User());

        service.sendMessage(message);
        return "redirect:/member/messages";
    }

    @PostMapping("/update")
    public String updateMessage(@ModelAttribute Message message) {
        service.updateMessage(message);
        return "redirect:/member/messages";
    }

    @PostMapping("/delete")
    public String deleteMessage(@RequestParam int id) {
        service.deleteMessage(id);
        return "redirect:/member/messages";
    }

    @GetMapping("/sender")
    public String getMessagesBySender(
            @RequestParam int senderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getMessagesBySender(senderId),
                page,
                size,
                "/member/messages/sender",
                "messages/result",
                model,
                Map.of("senderId", senderId));
    }

    @GetMapping("/receiver")
    public String getMessagesByReceiver(
            @RequestParam int receiverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getMessagesByReceiver(receiverId),
                page,
                size,
                "/member/messages/receiver",
                "messages/result",
                model,
                Map.of("receiverId", receiverId));
    }

    @GetMapping("/conversation")
    public String getConversation(@RequestParam int user1,
            @RequestParam int user2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getConversation(user1, user2),
                page,
                size,
                "/member/messages/conversation",
                "messages/result",
                model,
                Map.of("user1", user1, "user2", user2));
    }
}
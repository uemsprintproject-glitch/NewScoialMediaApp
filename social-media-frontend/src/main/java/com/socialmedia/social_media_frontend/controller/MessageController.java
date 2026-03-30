package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Message;
import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.MessageClientService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getAllMessages(Model model) {
        model.addAttribute("data", service.getAllMessages());
        return "messages/result";
    }

    @GetMapping("/by-id")
    public String getMessageById(@RequestParam int id, Model model) {

        Message message = null;

        try {
            message = service.getMessageById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (message == null) {
            model.addAttribute("data", List.of());
            model.addAttribute("error", "Message not found with ID: " + id);
        } else {
            model.addAttribute("data", List.of(message));
        }

        return "messages/result";
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
    public String getMessagesBySender(@RequestParam int senderId, Model model) {
        model.addAttribute("data", service.getMessagesBySender(senderId));
        return "messages/result";
    }

    @GetMapping("/receiver")
    public String getMessagesByReceiver(@RequestParam int receiverId, Model model) {
        model.addAttribute("data", service.getMessagesByReceiver(receiverId));
        return "messages/result";
    }

    @GetMapping("/conversation")
    public String getConversation(@RequestParam int user1,
            @RequestParam int user2,
            Model model) {
        model.addAttribute("data", service.getConversation(user1, user2));
        return "messages/result";
    }
}
package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Message;
import com.socialmedia.social_media_frontend.service.MessageClientService;

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
        model.addAttribute("message", service.getMessageById(id));
        return "messages/message-by-id";
    }

    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message) {
        service.sendMessage(message);
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
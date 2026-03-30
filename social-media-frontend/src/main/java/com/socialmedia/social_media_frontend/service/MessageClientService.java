package com.socialmedia.social_media_frontend.service;

import com.socialmedia.social_media_frontend.model.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MessageClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:8090/member/messages";

    public List<Message> getAllMessages() {
        Message[] messages = restTemplate.getForObject(BASE_URL + "/all", Message[].class);
        return messages == null ? Collections.emptyList() : Arrays.asList(messages);
    }

    public Message getMessageById(Integer id) {
        return restTemplate.getForObject(BASE_URL + "/by-id?id=" + id, Message.class);
    }

    // ✅ FIXED: /create instead of /send
    public void sendMessage(Message message) {
        restTemplate.postForObject(BASE_URL + "/create", message, Message.class);
    }

    // ✅ better REST
    public void updateMessage(Message message) {
        restTemplate.put(BASE_URL + "/update", message);
    }

    // ✅ better REST
    public void deleteMessage(Integer id) {
        restTemplate.delete(BASE_URL + "/delete?id=" + id);
    }

    public List<Message> getMessagesBySender(Integer senderId) {
        Message[] messages = restTemplate.getForObject(
                BASE_URL + "/by-sender?senderId=" + senderId,
                Message[].class);
        return messages == null ? Collections.emptyList() : Arrays.asList(messages);
    }

    public List<Message> getMessagesByReceiver(Integer receiverId) {
        Message[] messages = restTemplate.getForObject(
                BASE_URL + "/by-receiver?receiverId=" + receiverId,
                Message[].class);
        return messages == null ? Collections.emptyList() : Arrays.asList(messages);
    }

    public List<Message> getConversation(Integer user1, Integer user2) {
        Message[] messages = restTemplate.getForObject(
                BASE_URL + "/conversation?user1=" + user1 + "&user2=" + user2,
                Message[].class);
        return messages == null ? Collections.emptyList() : Arrays.asList(messages);
    }
}
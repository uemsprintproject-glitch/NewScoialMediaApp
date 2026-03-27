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
        Message[] messages = restTemplate.getForObject(BASE_URL, Message[].class);
        return messages == null ? Collections.emptyList() : Arrays.asList(messages);
    }

    public Message getMessageById(Integer id) {
        return restTemplate.getForObject(BASE_URL + "/" + id, Message.class);
    }

    public void sendMessage(Message message) {
        restTemplate.postForObject(BASE_URL, message, Message.class);
    }

    public void updateMessage(Message message) {
        restTemplate.postForObject(BASE_URL + "/update", message, Message.class);
    }

    public void deleteMessage(Integer id) {
        restTemplate.delete(BASE_URL + "/" + id);
    }

    public List<Message> getMessagesBySender(Integer senderId) {
        Message[] messages = restTemplate.getForObject(BASE_URL + "/sender/" + senderId, Message[].class);
        return messages == null ? Collections.emptyList() : Arrays.asList(messages);
    }

    public List<Message> getMessagesByReceiver(Integer receiverId) {
        Message[] messages = restTemplate.getForObject(BASE_URL + "/receiver/" + receiverId, Message[].class);
        return messages == null ? Collections.emptyList() : Arrays.asList(messages);
    }

    public List<Message> getConversation(Integer user1, Integer user2) {
        Message[] messages = restTemplate.getForObject(
                BASE_URL + "/conversation?user1=" + user1 + "&user2=" + user2,
                Message[].class);
        return messages == null ? Collections.emptyList() : Arrays.asList(messages);
    }
}
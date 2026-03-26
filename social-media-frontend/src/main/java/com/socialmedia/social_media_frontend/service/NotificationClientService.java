package com.socialmedia.social_media_frontend.service;

import com.socialmedia.social_media_frontend.model.Notification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:8090/member/notifications";

    public List<Notification> getAllNotifications() {
        Notification[] notifications = restTemplate.getForObject(BASE_URL + "/all", Notification[].class);
        return notifications == null ? Collections.emptyList() : Arrays.asList(notifications);
    }

    public Notification getNotificationById(Integer id) {
        return restTemplate.getForObject(BASE_URL + "/by-id?id=" + id, Notification.class);
    }

    public List<Notification> getNotificationsByUser(Integer userID) {
        Notification[] notifications = restTemplate.getForObject(BASE_URL + "/by-user?userID=" + userID,
                Notification[].class);
        return notifications == null ? Collections.emptyList() : Arrays.asList(notifications);
    }

    public void createNotification(Notification notification) {
        restTemplate.postForObject(BASE_URL + "/create", notification, Notification.class);
    }

    public void updateNotification(Notification notification) {
        restTemplate.postForObject(BASE_URL + "/update", notification, Notification.class);
    }

    public void deleteNotification(Integer id) {
        restTemplate.postForObject(BASE_URL + "/delete?id=" + id, null, Void.class);
    }
}

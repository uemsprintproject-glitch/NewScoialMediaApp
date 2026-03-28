package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.model.Notification;
import com.socialmedia.social_media_backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Notification getNotificationById(Integer id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public List<Notification> getNotificationsByUser(Integer userID) {
        return notificationRepository.findByUserUserID(userID);
    }

    public Notification createNotification(Notification notification) {
        notification.setNotificationID(null);
        notification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(Notification updatedNotification) {
        Notification existingNotification = notificationRepository.findById(updatedNotification.getNotificationID())
                .orElseThrow();

        existingNotification.setContent(updatedNotification.getContent());
        existingNotification.setUser(updatedNotification.getUser());
        existingNotification.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        return notificationRepository.save(existingNotification);
    }

    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }
}
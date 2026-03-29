package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Notification;
import com.socialmedia.social_media_backend.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Notification> getAllNotifications() {
        return service.getAllNotifications();
    }

    @GetMapping("/by-id")
    public Notification getById(@RequestParam Integer id) {
        return service.getNotificationById(id);
    }

    @GetMapping("/by-user")
    public List<Notification> getByUser(@RequestParam Integer userID) {
        return service.getNotificationsByUser(userID);
    }

    @PostMapping("/create")
    public Notification create(@RequestBody Notification notification) {
        return service.createNotification(notification);
    }

    @PostMapping("/update")
    public Notification update(@RequestBody Notification notification) {
        return service.updateNotification(notification);
    }

    @PostMapping("/delete")
    public void delete(@RequestParam Integer id) {
        service.deleteNotification(id);
    }
}

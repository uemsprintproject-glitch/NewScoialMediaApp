package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Notification;
import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.NotificationClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/member/notifications")
public class NotificationController {

    private final NotificationClientService service;

    public NotificationController(NotificationClientService service) {
        this.service = service;
    }

    @GetMapping
    public String page() {
        return "notifications/notifications";
    }

    @GetMapping("/all")
    public String getAllNotifications(Model model) {
        model.addAttribute("data", service.getAllNotifications());
        return "notifications/result";
    }

    @GetMapping("/by-id")
    public String getById(@RequestParam Integer id, Model model) {
        Notification notification = service.getNotificationById(id);
        model.addAttribute("data", notification == null ? Collections.emptyList() : List.of(notification));
        return "notifications/result";
    }

    @GetMapping("/by-user")
    public String getByUser(@RequestParam Integer userID, Model model) {
        model.addAttribute("data", service.getNotificationsByUser(userID));
        return "notifications/result";
    }

    @GetMapping("/by-id-form")
    public String getByIdForm() {
        return "notifications/get-by-id";
    }

    @GetMapping("/by-user-form")
    public String getByUserForm() {
        return "notifications/get-by-user";
    }

    @GetMapping("/create-form")
    public String getCreateForm() {
        return "notifications/create-notification";
    }

    @GetMapping("/update-form")
    public String getUpdateForm() {
        return "notifications/update-notification";
    }

    @GetMapping("/delete-form")
    public String getDeleteForm() {
        return "notifications/delete-notification";
    }

    @PostMapping("/create")
    public String createNotification(@RequestParam String content,
            @RequestParam Integer userID) {
        service.createNotification(buildCreateNotification(content, userID));
        return "redirect:/member/notifications";
    }

    @PostMapping("/update")
    public String updateNotification(@RequestParam Integer notificationID,
            @RequestParam String content,
            @RequestParam Integer userID) {
        service.updateNotification(buildUpdateNotification(notificationID, content, userID));
        return "redirect:/member/notifications";
    }

    @PostMapping("/delete")
    public String deleteNotification(@RequestParam Integer id) {
        service.deleteNotification(id);
        return "redirect:/member/notifications";
    }

    private Notification buildCreateNotification(String content, Integer userID) {
        return buildNotification(null, content, userID);
    }

    private Notification buildUpdateNotification(Integer notificationID, String content, Integer userID) {
        return buildNotification(notificationID, content, userID);
    }

    private Notification buildNotification(Integer notificationID, String content, Integer userID) {
        User user = new User();
        user.setUserID(userID);

        Notification notification = new Notification();
        notification.setNotificationID(notificationID);
        notification.setContent(content);
        notification.setUser(user);
        return notification;
    }
}

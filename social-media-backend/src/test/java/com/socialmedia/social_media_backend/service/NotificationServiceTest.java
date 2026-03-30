package com.socialmedia.social_media_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.socialmedia.social_media_backend.model.Notification;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.NotificationRepository;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository repo;

    @InjectMocks
    private NotificationService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllNotifications() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Notification n1 = new Notification(1, "Liked your post", Timestamp.valueOf("2026-03-29 10:00:00"), user);
        Notification n2 = new Notification(2, "Commented on your post", Timestamp.valueOf("2026-03-29 10:05:00"), user);

        when(repo.findAll()).thenReturn(List.of(n1, n2));

        List<Notification> result = service.getAllNotifications();
        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    public void testGetNotificationById() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Notification notification = new Notification(1, "Liked your post", Timestamp.valueOf("2026-03-29 10:00:00"),
                user);

        when(repo.findById(1)).thenReturn(Optional.of(notification));

        Notification result = service.getNotificationById(1);
        assertEquals("Liked your post", result.getContent());
        verify(repo).findById(1);
    }

    @Test
    public void testGetNotificationsByUser() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Notification n1 = new Notification(1, "Liked your post", Timestamp.valueOf("2026-03-29 10:00:00"), user);
        Notification n2 = new Notification(2, "Commented on your post", Timestamp.valueOf("2026-03-29 10:05:00"), user);

        when(repo.findByUserUserID(1)).thenReturn(List.of(n1, n2));

        List<Notification> result = service.getNotificationsByUser(1);
        assertEquals(2, result.size());
        verify(repo).findByUserUserID(1);
    }

    @Test
    public void testCreateNotification() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Notification notification = new Notification(99, "New friend request", Timestamp.valueOf("2026-03-29 09:00:00"),
                user);

        when(repo.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification saved = invocation.getArgument(0);
            saved.setNotificationID(1);
            return saved;
        });

        Notification result = service.createNotification(notification);
        assertEquals(1, result.getNotificationID());
        assertNotNull(result.getTimestamp());
        verify(repo).save(any(Notification.class));
    }

    @Test
    public void testUpdateNotification() {
        User oldUser = new User(1, "old", "old@gmail.com", "123", null, null, null);
        User newUser = new User(2, "new", "new@gmail.com", "456", null, null, null);

        Notification existing = new Notification(1, "Old content", Timestamp.valueOf("2026-03-29 09:00:00"), oldUser);
        Notification updated = new Notification(1, "New content", null, newUser);

        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Notification result = service.updateNotification(updated);
        assertEquals("New content", result.getContent());
        assertEquals(2, result.getUser().getUserID());
        assertNotNull(result.getTimestamp());
        verify(repo).findById(1);
        verify(repo).save(existing);
    }

    @Test
    public void testDeleteNotification() {
        service.deleteNotification(1);
        verify(repo).deleteById(1);
    }
}
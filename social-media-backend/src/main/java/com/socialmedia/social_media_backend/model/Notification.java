package com.socialmedia.social_media_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationID;

    private String content;

    private java.sql.Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
}

package com.socialmedia.social_media_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer notificationID;

    private String content;

    private java.sql.Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
}

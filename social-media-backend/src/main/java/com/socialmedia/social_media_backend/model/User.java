package com.socialmedia.social_media_backend.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    private String username;
    private String email;
    private String password;

    @Column(nullable = true)
    private String profilePicture;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments;

    // @OneToMany(mappedBy = "user")
    // @JsonIgnore
    // private List<Like> likes;

    // @OneToMany(mappedBy = "sender")
    // @JsonIgnore
    // private List<Message> sentMessages;

    // @OneToMany(mappedBy = "receiver")
    // @JsonIgnore
    // private List<Message> receivedMessages;

    // @OneToMany(mappedBy = "user")
    // @JsonIgnore
    // private List<Notification> notifications;
}
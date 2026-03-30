package com.socialmedia.social_media_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageID;

    private String message_text;

    @CreationTimestamp
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "senderID")
    @JsonIgnoreProperties({ "posts", "comments", "likes" })
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiverID")
    @JsonIgnoreProperties({ "posts", "comments", "likes" })
    private User receiver;
}
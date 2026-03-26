package com.socialmedia.social_media_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    private Integer messageID;

    private String message_text;


    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;
}
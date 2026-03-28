package com.socialmedia.social_media_frontend.model;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    private Integer messageID;
    private String message_text;
    private Timestamp timestamp;

    private User sender = new User();
    private User receiver = new User();
}
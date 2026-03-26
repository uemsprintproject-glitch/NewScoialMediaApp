package com.socialmedia.social_media_frontend.model;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    private Integer postID;
    private String content;
    private Timestamp timestamp;

    private User user;
}
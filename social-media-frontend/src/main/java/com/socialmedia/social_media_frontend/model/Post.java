package com.socialmedia.social_media_frontend.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Post {
    private Integer postID;
    private String content;
    private Timestamp timestamp;
    private User user;
}
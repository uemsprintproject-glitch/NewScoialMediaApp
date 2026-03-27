package com.socialmedia.social_media_frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.socialmedia.social_media_frontend.model.Post;
import com.socialmedia.social_media_frontend.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentID;

    @Column(nullable = false)
    private String comment_text;

    private java.sql.Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "postID", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
}
package com.socialmedia.social_media_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    private Integer likeID;

    private java.sql.Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "postID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
}

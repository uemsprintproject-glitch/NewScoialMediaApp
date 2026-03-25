package com.socialmedia.social_media_frontend.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Friends")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend {

    @Id
    private Integer friendshipID;

    @ManyToOne
    @JoinColumn(name = "userID1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "userID2")
    private User user2;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        pending,
        accepted
    }
}

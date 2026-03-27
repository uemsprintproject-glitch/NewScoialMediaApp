package com.socialmedia.social_media_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`Groups`")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupID;

    @Column(name = "groupName", nullable = false, unique = true)
    private String groupName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adminID", nullable = false)
    private User admin;
}

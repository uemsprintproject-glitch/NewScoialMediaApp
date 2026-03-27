package com.socialmedia.social_media_backend.repository;

import com.socialmedia.social_media_backend.model.Friend;
import com.socialmedia.social_media_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    List<Friend> findByUser1(User user1);
    List<Friend> findByUser2(User user2);
    List<Friend> findByUser1AndStatus(User user1, Friend.Status status);
    List<Friend> findByUser2AndStatus(User user2, Friend.Status status);
    Optional<Friend> findByUser1AndUser2(User user1, User user2);
    Optional<Friend> findByUser2AndUser1(User user2, User user1);
}

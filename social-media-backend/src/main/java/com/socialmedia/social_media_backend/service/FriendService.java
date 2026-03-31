package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.exception.ResourceNotFoundException;
import com.socialmedia.social_media_backend.model.Friend;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.FriendRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public List<Friend> getAllFriends() {
        return friendRepository.findAll();
    }

    public Friend getFriendById(Integer friendshipId) {
        return friendRepository.findById(friendshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Friendship not found"));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<Friend> getPendingRequests(Integer userId) {
        User user = getUserById(userId);
        return friendRepository.findByUser2AndStatus(user, Friend.Status.pending);
    }

    public List<Friend> getSentRequests(Integer userId) {
        User user = getUserById(userId);
        return friendRepository.findByUser1AndStatus(user, Friend.Status.pending);
    }

    public Friend sendFriendRequest(Integer userId1, Integer userId2) {
        if (userId1.equals(userId2)) {
            throw new RuntimeException("Cannot send friend request to self");
        }

        User user1 = getUserById(userId1);
        User user2 = getUserById(userId2);

        Optional<Friend> existing = friendRepository.findByUser1AndUser2(user1, user2);
        if (existing.isEmpty()) {
            existing = friendRepository.findByUser2AndUser1(user2, user1);
        }
        if (existing.isPresent()) {
            return existing.get();
        }

        Friend friend = Friend.builder()
                .user1(user1)
                .user2(user2)
                .status(Friend.Status.pending)
                .build();
        return friendRepository.save(friend);
    }

    public Friend acceptFriendRequest(Integer friendshipId) {
        Friend friend = getFriendById(friendshipId);
        friend.setStatus(Friend.Status.accepted);
        return friendRepository.save(friend);
    }

    public void unsendFriendRequest(Integer friendshipId) {
        System.out.println(friendshipId);
        friendRepository.deleteById(friendshipId);
    }

    public String removeFriend(Integer friendshipId) {
        friendRepository.deleteById(friendshipId);
        return "Friend Removed";
    }

    public Boolean areFriends(Integer userId1, Integer userId2) {
        User user1 = getUserById(userId1);
        User user2 = getUserById(userId2);

        Optional<Friend> friendship = friendRepository.findByUser1AndUser2(user1, user2);
        if (friendship.isEmpty()) {
            friendship = friendRepository.findByUser2AndUser1(user2, user1);
        }

        return friendship.isPresent() && friendship.get().getStatus() == Friend.Status.accepted;
    }
}

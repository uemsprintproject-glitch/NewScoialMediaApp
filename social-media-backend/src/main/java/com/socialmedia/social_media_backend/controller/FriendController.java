package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Friend;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.service.FriendService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/friends")
public class FriendController {

    private final FriendService service;

    public FriendController(FriendService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Friend> getAllFriends() {
        return service.getAllFriends();
    }

    @GetMapping("/by-id")
    public Friend getFriendById(@RequestParam Integer friendshipId) {
        return service.getFriendById(friendshipId);
    }

    @GetMapping("/user/by-id")
    public User getUserById(@RequestParam Integer userId) {
        return service.getUserById(userId);
    }

    @GetMapping("/by-user")
    public List<User> getFriendsByUser(@RequestParam Integer userId) {
        return service.getFriendsByUser(userId);
    }

    @GetMapping("/requests/pending")
    public List<Friend> getPendingRequests(@RequestParam Integer userId) {
        return service.getPendingRequests(userId);
    }

    @GetMapping("/requests/sent")
    public List<Friend> getSentRequests(@RequestParam Integer userId) {
        return service.getSentRequests(userId);
    }

    @GetMapping("/check")
    public Boolean checkFriendship(@RequestParam Integer userId1, @RequestParam Integer userId2) {
        return service.areFriends(userId1, userId2);
    }

    @PostMapping("/send")
    public Friend sendFriendRequest(@RequestParam Integer userId1, @RequestParam Integer userId2) {
        return service.sendFriendRequest(userId1, userId2);
    }

    @PostMapping("/accept")
    public Friend acceptFriendRequest(@RequestParam Integer friendshipId) {
        return service.acceptFriendRequest(friendshipId);
    }

    @PostMapping("/unsend")
    public void unsendFriendRequest(@RequestParam Integer friendshipId) {
        service.unsendFriendRequest(friendshipId);
    }

    @PostMapping("/remove")
    public void removeFriend(@RequestParam Integer friendshipId) {
        service.removeFriend(friendshipId);
    }
}

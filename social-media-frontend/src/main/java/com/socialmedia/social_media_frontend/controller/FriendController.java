package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Friend;
import com.socialmedia.social_media_frontend.service.FriendClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member/friends")
public class FriendController {

    private final FriendClientService service;

    public FriendController(FriendClientService service) {
        this.service = service;
    }

    @GetMapping
    public String page() {
        return "friends/friends";
    }

    @GetMapping("/all")
    public String getAllFriends(Model model) {
        model.addAttribute("data", service.getAllFriends());
        return "friends/result";
    }

    @GetMapping("/by-id")
    public String getFriendById(@RequestParam Integer friendshipId, Model model) {
        Friend friend = service.getFriendById(friendshipId);
        model.addAttribute("friend", friend);
        return "friends/friend-by-id";
    }

    @GetMapping("/requests/pending")
    public String getPendingRequests(@RequestParam Integer userId, Model model) {
        model.addAttribute("data", service.getPendingRequests(userId));
        return "friends/result";
    }

    @GetMapping("/requests/sent")
    public String getSentRequests(@RequestParam Integer userId, Model model) {
        model.addAttribute("data", service.getSentRequests(userId));
        return "friends/result";
    }

    @GetMapping("/check")
    public String checkFriendship(@RequestParam Integer userId1, @RequestParam Integer userId2, Model model) {
        Boolean isFriend = service.checkFriendship(userId1, userId2);
        model.addAttribute("friend", null);
        model.addAttribute("resultMessage", Boolean.TRUE.equals(isFriend) ? "Users are friends" : "Users are not friends");
        return "friends/friend-by-id";
    }

    @PostMapping("/send")
    public String sendFriendRequest(@RequestParam Integer userId1, @RequestParam Integer userId2) {
        service.sendFriendRequest(userId1, userId2);
        return "redirect:/member/friends";
    }

    @PostMapping("/accept")
    public String acceptFriendRequest(@RequestParam Integer friendshipId) {
        service.acceptFriendRequest(friendshipId);
        return "redirect:/member/friends";
    }

    @PostMapping("/unsend")
    public String unsendFriendRequest(@RequestParam Integer friendshipId) {
        service.unsendFriendRequest(friendshipId);
        return "redirect:/member/friends";
    }

    @PostMapping("/remove")
    public String removeFriend(@RequestParam Integer friendshipId) {
        service.removeFriend(friendshipId);
        return "redirect:/member/friends";
    }
}
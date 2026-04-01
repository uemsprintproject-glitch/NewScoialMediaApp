package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Friend;
import com.socialmedia.social_media_frontend.service.FriendClientService;
import com.socialmedia.social_media_frontend.util.PaginationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

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
    public String getAllFriends(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getAllFriends(),
                page,
                size,
                "/member/friends/all",
                "friends/result",
                model,
                Map.of());
    }

    @GetMapping("/by-id")
    public String getFriendById(@RequestParam Integer friendshipId, Model model) {
        Friend friend = service.getFriendById(friendshipId);
        if (friend == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Friendship not found with ID: " + friendshipId);
        }
        model.addAttribute("friend", friend);
        return "friends/friend-by-id";
    }

    @GetMapping("/requests/pending")
    public String getPendingRequests(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getPendingRequests(userId),
                page,
                size,
                "/member/friends/requests/pending",
                "friends/result",
                model,
                Map.of("userId", userId));
    }

    @GetMapping("/requests/sent")
    public String getSentRequests(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getSentRequests(userId),
                page,
                size,
                "/member/friends/requests/sent",
                "friends/result",
                model,
                Map.of("userId", userId));
    }

    @GetMapping("/check")
    public String checkFriendship(@RequestParam Integer userId1, @RequestParam Integer userId2, Model model) {
        Boolean isFriend = service.checkFriendship(userId1, userId2);
        model.addAttribute("friend", null);
        model.addAttribute("resultMessage",
                Boolean.TRUE.equals(isFriend) ? "Users are friends" : "Users are not friends");
        return "friends/friend-by-id";
    }

    @GetMapping("/by-id-form")
    public String getByIdForm() {
        return "friends/get-by-id";
    }

    @GetMapping("/pending-form")
    public String getPendingForm() {
        return "friends/get-pending";
    }

    @GetMapping("/sent-form")
    public String getSentForm() {
        return "friends/get-sent";
    }

    @GetMapping("/check-form")
    public String getCheckForm() {
        return "friends/check-friendship";
    }

    @GetMapping("/send-form")
    public String getSendForm() {
        return "friends/send-request";
    }

    @GetMapping("/accept-form")
    public String getAcceptForm() {
        return "friends/accept-request";
    }

    @GetMapping("/unsend-form")
    public String getUnsendForm() {
        return "friends/unsend-request";
    }

    @GetMapping("/remove-form")
    public String getRemoveForm() {
        return "friends/remove-friend";
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
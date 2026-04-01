package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Like;
import com.socialmedia.social_media_frontend.model.Post;
import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.LikeClientService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/member/likes")
public class LikeController {

    private final LikeClientService service;

    public LikeController(LikeClientService service) {
        this.service = service;
    }

    @GetMapping
    public String page() {
        return "likes/likes";
    }

    @GetMapping("/all")
    public String getAllLikes(Model model) {
        model.addAttribute("data", service.getAllLikes());
        return "likes/result";
    }

    @GetMapping("/by-id")
    public String getLikeById(@RequestParam Integer id, Model model) {
        Like like = service.getLikeById(id);
        if (like == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found with ID: " + id);
        }
        model.addAttribute("data", List.of(like));
        return "likes/result";
    }

    @GetMapping("/by-user")
    public String getByUser(@RequestParam Integer userID, Model model) {
        List<Like> likes = service.getLikesByUser(userID);
        if (likes == null || likes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No likes found for user ID: " + userID);
        }
        model.addAttribute("data", likes);
        return "likes/result";
    }

    @GetMapping("/by-post")
    public String getByPost(@RequestParam Integer postID, Model model) {
        model.addAttribute("data", service.getLikesByPost(postID));
        return "likes/result";
    }

    @GetMapping("/by-id-form")
    public String getByIdForm() {
        return "likes/get-by-id";
    }

    @GetMapping("/by-user-form")
    public String getByUserForm() {
        return "likes/get-by-user";
    }

    @GetMapping("/by-post-form")
    public String getByPostForm() {
        return "likes/get-by-post";
    }

    @GetMapping("/create-form")
    public String getCreateForm() {
        return "likes/create-like";
    }

    @GetMapping("/update-form")
    public String getUpdateForm() {
        return "likes/update-like";
    }

    @GetMapping("/delete-form")
    public String getDeleteForm() {
        return "likes/delete-like";
    }

    @PostMapping("/create")
    public String createLike(@RequestParam Integer userID,
            @RequestParam Integer postID) {
        service.createLike(buildCreateLike(userID, postID));
        return "redirect:/member/likes";
    }

    @PostMapping("/update")
    public String updateLike(@RequestParam Integer likeID,
            @RequestParam Integer userID,
            @RequestParam Integer postID) {
        service.updateLike(buildUpdateLike(likeID, userID, postID));
        return "redirect:/member/likes";
    }

    @PostMapping("/delete")
    public String deleteLike(@RequestParam Integer id) {
        service.deleteLike(id);
        return "redirect:/member/likes";
    }

    private Like buildCreateLike(Integer userID, Integer postID) {
        return buildLike(null, userID, postID);
    }

    private Like buildUpdateLike(Integer likeID, Integer userID, Integer postID) {
        return buildLike(likeID, userID, postID);
    }

    private Like buildLike(Integer likeID, Integer userID, Integer postID) {
        User user = new User();
        user.setUserID(userID);

        Post post = new Post();
        post.setPostID(postID);

        Like like = new Like();
        like.setLikeID(likeID);
        like.setUser(user);
        like.setPost(post);
        return like;
    }
}

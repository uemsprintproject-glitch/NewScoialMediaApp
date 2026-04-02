package com.socialmedia.social_media_backend.controller;

import com.socialmedia.social_media_backend.model.Like;
import com.socialmedia.social_media_backend.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/likes")
public class LikeController {

    private final LikeService service;

    public LikeController(LikeService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Like> getAllLikes() {
        return service.getAllLikes();
    }

    @GetMapping("/by-id")
    public Like getById(@RequestParam Integer id) {
        return service.getLikeById(id);
    }

    @GetMapping("/by-user")
    public List<Like> getByUser(@RequestParam Integer userID) {
        return service.getLikesByUser(userID);
    }

    @GetMapping("/by-post")
    public List<Like> getByPost(@RequestParam Integer postID) {
        return service.getLikesByPost(postID);
    }

    @PostMapping("/create")
    public Like create(@RequestBody Like like) {
        return service.createLike(like);
    }

    @PutMapping("/update")
    public Like update(@RequestBody Like like) {
        return service.updateLike(like);
    }

    @PostMapping("/delete")
    public void delete(@RequestParam Integer id) {
        service.deleteLike(id);
    }
}

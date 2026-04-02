package com.socialmedia.social_media_frontend.service;

import com.socialmedia.social_media_frontend.model.Like;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LikeClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "http://localhost:8090/member/likes";

    public List<Like> getAllLikes() {
        Like[] likes = restTemplate.getForObject(BASE_URL + "/all", Like[].class);
        return likes == null ? Collections.emptyList() : Arrays.asList(likes);
    }

    public Like getLikeById(Integer id) {
        return restTemplate.getForObject(BASE_URL + "/by-id?id=" + id, Like.class);
    }

    public List<Like> getLikesByUser(Integer userID) {
        Like[] likes = restTemplate.getForObject(BASE_URL + "/by-user?userID=" + userID, Like[].class);
        return likes == null ? Collections.emptyList() : Arrays.asList(likes);
    }

    public List<Like> getLikesByPost(Integer postID) {
        Like[] likes = restTemplate.getForObject(BASE_URL + "/by-post?postID=" + postID, Like[].class);
        return likes == null ? Collections.emptyList() : Arrays.asList(likes);
    }

    public void createLike(Like like) {
        restTemplate.postForObject(BASE_URL + "/create", like, Like.class);
    }

    public void updateLike(Like like) {
        restTemplate.put(BASE_URL + "/update", like);
    }

    public void deleteLike(Integer id) {
        restTemplate.postForObject(BASE_URL + "/delete?id=" + id, null, Void.class);
    }
}

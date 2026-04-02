package com.socialmedia.social_media_frontend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.socialmedia.social_media_frontend.model.Post;

import java.util.Arrays;
import java.util.List;

@Service
public class PostClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String BASE_URL = "http://localhost:8090/member/posts";

    public List<Post> getAllPosts() {
        Post[] posts = restTemplate.getForObject(BASE_URL + "/all", Post[].class);
        return Arrays.asList(posts);
    }

    public List<Post> getPostsByUser(int userId) {
        Post[] posts = restTemplate.getForObject(
                BASE_URL + "/by-user?userId=" + userId,
                Post[].class);
        return Arrays.asList(posts);
    }

    public void createPost(int userId, String content) {
        restTemplate.postForObject(
                BASE_URL + "/create?userId={userId}&content={content}",
                null,
                Post.class,
                userId,
                content);
    }

    public void deletePost(int postId) {
        restTemplate.postForObject(
                BASE_URL + "/delete?postId=" + postId,
                null,
                Void.class);
    }

    public void updatePost(Post post) {
        restTemplate.put(BASE_URL + "/update", post);
    }

    public Post getPostById(int id) {
        return restTemplate.getForObject(BASE_URL + "/by-id?postId=" + id, Post.class);
    }

    public List<Post> getSortedPosts(String dir) {
        Post[] posts = restTemplate.getForObject(
                BASE_URL + "/sorted?dir=" + dir,
                Post[].class);
        return Arrays.asList(posts);
    }
}
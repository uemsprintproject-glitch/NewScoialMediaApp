package com.socialmedia.social_media_frontend.service;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.socialmedia.social_media_frontend.model.Post;
import com.socialmedia.social_media_frontend.model.User;

@Service
public class UserClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String BASE_URL = "http://localhost:8090/member/users";

    public List<User> getAllUsers() {
        User[] users = restTemplate.getForObject(BASE_URL + "/all", User[].class);
        return Arrays.asList(users);
    }

    public User getUserById(int id) {
        return restTemplate.getForObject(BASE_URL + "/by-id?id=" + id, User.class);
    }

    public void createUser(User user) {
        restTemplate.postForObject(BASE_URL + "/create", user, User.class);
    }

    public void updateUser(User user) {
        restTemplate.postForObject(BASE_URL + "/update", user, User.class);
    }

    public void deleteUser(int id) {
        restTemplate.postForObject(BASE_URL + "/delete?id=" + id, null, Void.class);
    }

    public List<Post> getPostsByUser(int userId) {
        Post[] posts = restTemplate.getForObject(BASE_URL + "/posts?id=" + userId, Post[].class);
        return Arrays.asList(posts);
    }

    public List<User> getUserByUsername(String username) {
        User[] user = restTemplate.getForObject(BASE_URL + "/username?username=" + username, User[].class);
        return user == null ? Collections.emptyList() : Arrays.asList(user);
    }

    public User getUserByEmail(String email) {
        return restTemplate.getForObject(BASE_URL + "/email?email=" + email, User.class);
    }

    public User getUserByPost(int id) {
        return restTemplate.getForObject(BASE_URL + "/posts?id=" + id, User.class);
    }

    public User getUserByComment(Integer id) {
        return restTemplate.getForObject(BASE_URL + "/comment?id=" + id, User.class);
    }
}
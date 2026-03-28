package com.socialmedia.social_media_frontend.service;

import com.socialmedia.social_media_frontend.model.Friend;
import com.socialmedia.social_media_frontend.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class FriendClientService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8090/member/friends";

    public List<Friend> getAllFriends() {
        Friend[] friends = restTemplate.getForObject(BASE_URL + "/all", Friend[].class);
        return friends == null ? List.of() : Arrays.asList(friends);
    }

    public Friend getFriendById(Integer friendshipId) {
        return restTemplate.getForObject(BASE_URL + "/by-id?friendshipId=" + friendshipId, Friend.class);
    }

    public User getUserById(Integer userId) {
        return restTemplate.getForObject(BASE_URL + "/user/by-id?userId=" + userId, User.class);
    }

    public List<User> getFriendsByUser(Integer userId) {
        User[] users = restTemplate.getForObject(BASE_URL + "/by-user?userId=" + userId, User[].class);
        return users == null ? List.of() : Arrays.asList(users);
    }

    public List<Friend> getPendingRequests(Integer userId) {
        Friend[] friends = restTemplate.getForObject(BASE_URL + "/requests/pending?userId=" + userId, Friend[].class);
        return friends == null ? List.of() : Arrays.asList(friends);
    }

    public List<Friend> getSentRequests(Integer userId) {
        Friend[] friends = restTemplate.getForObject(BASE_URL + "/requests/sent?userId=" + userId, Friend[].class);
        return friends == null ? List.of() : Arrays.asList(friends);
    }

    public Boolean checkFriendship(Integer userId1, Integer userId2) {
        return restTemplate.getForObject(BASE_URL + "/check?userId1=" + userId1 + "&userId2=" + userId2, Boolean.class);
    }

    public void sendFriendRequest(Integer userId1, Integer userId2) {
        restTemplate.postForObject(BASE_URL + "/send?userId1=" + userId1 + "&userId2=" + userId2, null, Friend.class);
    }

    public void acceptFriendRequest(Integer friendshipId) {
        restTemplate.postForObject(BASE_URL + "/accept?friendshipId=" + friendshipId, null, Friend.class);
    }

    public void unsendFriendRequest(Integer friendshipId) {
        restTemplate.postForObject(BASE_URL + "/unsend?friendshipId=" + friendshipId, null, Void.class);
    }

    public void removeFriend(Integer friendshipId) {
        restTemplate.postForObject(BASE_URL + "/remove?friendshipId=" + friendshipId, null, Void.class);
    }
}
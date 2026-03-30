package com.socialmedia.social_media_frontend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.socialmedia.social_media_frontend.model.Comment;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String BASE_URL = "http://localhost:8090/member/comments";

    public List<Comment> getAllComments() {
        Comment[] comments = restTemplate.getForObject(BASE_URL + "/all", Comment[].class);
        return Arrays.asList(comments);
    }

    public Comment getCommentById(int id) {
        return restTemplate.getForObject(BASE_URL + "/by-id?id=" + id, Comment.class);
    }

    public void createComment(Comment comment) {
        restTemplate.postForObject(BASE_URL + "/create", comment, Comment.class);
    }

    public void updateComment(Comment comment) {
        restTemplate.postForObject(BASE_URL + "/update", comment, Comment.class);
    }

    public void deleteComment(int id) {
        restTemplate.postForObject(BASE_URL + "/delete?id=" + id, null, Void.class);
    }

    public List<Comment> getCommentsByPost(int postId) {
        Comment[] comments = restTemplate.getForObject(
                BASE_URL + "/by-post?postId=" + postId,
                Comment[].class
        );
        return Arrays.asList(comments);
    }

    public List<Comment> getCommentsByUser(int userId) {
        Comment[] comments = restTemplate.getForObject(
                BASE_URL + "/by-user?userId=" + userId,
                Comment[].class
        );
        return Arrays.asList(comments);
    }
    public List<Comment> getSortedComments(String dir) {
        Comment[] comments = restTemplate.getForObject(
                BASE_URL + "/sorted?dir=" + dir,
                Comment[].class
        );
        return Arrays.asList(comments);
    }



}
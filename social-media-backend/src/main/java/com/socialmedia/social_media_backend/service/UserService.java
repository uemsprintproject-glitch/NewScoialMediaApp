package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User getUserById(int id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        return repo.save(user);
    }

    public User updateUser(User updatedUser) {
        User existingUser = repo.findById(updatedUser.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setProfilePicture(updatedUser.getProfilePicture());

        return repo.save(existingUser);
    }

    public void deleteUser(int id) {
        repo.deleteById(id);
    }

    public List<Post> getPostsByUser(int userId) {
        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getPosts();
    }

    public List<User> getUserByUsername(String username){
        return repo.findByUsername(username);
    }

    public User getUserByEmail(String email){
        return repo.findByEmail(email);
    }

    public User getUserByPostId(int id){
        return repo.findByPostsPostID(id);
    }

    public User getUserByComment(Integer id){
        return repo.findByCommentsCommentID(id);
    }
}
package com.socialmedia.social_media_backend.service;

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

    // ✅ GET ALL
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    // ✅ GET BY ID
    public User getUserById(int id) {
        return repo.findById(id).orElse(null);
    }

    // ✅ CREATE
    public User createUser(User user) {
        return repo.save(user);
    }

    // ✅ UPDATE
    public User updateUser(User updatedUser) {
        User existingUser = repo.findById(updatedUser.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setProfilePicture(updatedUser.getProfilePicture());

        return repo.save(existingUser); // ✅ FIXED
    }

    // ✅ DELETE
    public void deleteUser(int id) {
        repo.deleteById(id);
    }
}
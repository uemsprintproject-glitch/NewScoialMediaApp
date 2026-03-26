package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.model.Like;
import com.socialmedia.social_media_backend.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    public Like getLikeById(Integer id) {
        return likeRepository.findById(id).orElse(null);
    }

    public List<Like> getLikesByUser(Integer userID) {
        return likeRepository.findByUserUserID(userID);
    }

    public List<Like> getLikesByPost(Integer postID) {
        return likeRepository.findByPostPostID(postID);
    }

    public Like createLike(Like like) {
        like.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return likeRepository.save(like);
    }

    public Like updateLike(Like updatedLike) {
        Like existingLike = likeRepository.findById(updatedLike.getLikeID())
                .orElseThrow();

        existingLike.setUser(updatedLike.getUser());
        existingLike.setPost(updatedLike.getPost());
        existingLike.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        return likeRepository.save(existingLike);
    }

    public void deleteLike(Integer id) {
        likeRepository.deleteById(id);
    }
}

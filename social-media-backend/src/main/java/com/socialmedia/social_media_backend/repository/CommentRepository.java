package com.socialmedia.social_media_backend.repository;

import com.socialmedia.social_media_backend.model.Comment;
import com.socialmedia.social_media_backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByPost(Post post);
}

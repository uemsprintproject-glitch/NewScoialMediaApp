package com.socialmedia.social_media_backend.repository;

import com.socialmedia.social_media_backend.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    List<Like> findByUserUserID(Integer userID);

    List<Like> findByPostPostID(Integer postID);
}

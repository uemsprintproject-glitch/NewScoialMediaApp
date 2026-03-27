package com.socialmedia.social_media_backend.repository;
import com.socialmedia.social_media_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);

    List<User> findByUsername(String username);
}

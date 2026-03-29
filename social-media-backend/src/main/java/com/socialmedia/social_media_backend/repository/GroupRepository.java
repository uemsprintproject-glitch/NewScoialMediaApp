package com.socialmedia.social_media_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialmedia.social_media_backend.model.Group;
import com.socialmedia.social_media_backend.model.User;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    List<Group> findByAdmin(User admin);

    List<Group> findByGroupNameContainingIgnoreCase(String groupName);
    
    Optional<Group> findByGroupNameIgnoreCase(String groupName);

}

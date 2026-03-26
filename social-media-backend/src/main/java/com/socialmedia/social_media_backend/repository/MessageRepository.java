package com.socialmedia.social_media_backend.repository;

import com.socialmedia.social_media_backend.model.Message;
import com.socialmedia.social_media_backend.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findBySender(User sender);

    List<Message> findByReceiver(User receiver);

    List<Message> findBySenderAndReceiver(User sender, User receiver);

    List<Message> findBySenderAndReceiverOrSenderAndReceiver(
            User sender1, User receiver1,
            User sender2, User receiver2);
}
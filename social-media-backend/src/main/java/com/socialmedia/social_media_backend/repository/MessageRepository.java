package com.socialmedia.social_media_backend.repository;

import com.socialmedia.social_media_backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findBySender_UserID(Integer senderId);

    List<Message> findByReceiver_UserID(Integer receiverId);

    List<Message> findBySender_UserIDAndReceiver_UserID(Integer senderId, Integer receiverId);

    List<Message> findBySender_UserIDAndReceiver_UserIDOrSender_UserIDAndReceiver_UserID(
            Integer sender1, Integer receiver1,
            Integer sender2, Integer receiver2);
}
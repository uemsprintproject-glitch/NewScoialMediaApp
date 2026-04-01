package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.exception.ResourceNotFoundException;
import com.socialmedia.social_media_backend.model.Message;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public List<Message> getAllMessages() {
        System.out.println("Hello");
        return messageRepository.findAll();
    }

    public Message getMessageByIdSafe(Integer id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with ID: " + id));
    }

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message updateMessage(Message updatedMessage) {

        Optional<Message> optionalMessage = messageRepository.findById(updatedMessage.getMessageID());

        if (optionalMessage.isPresent()) {

            Message existingMessage = optionalMessage.get();

            if (updatedMessage.getMessage_text() != null) {
                existingMessage.setMessage_text(updatedMessage.getMessage_text());
            }

            if (updatedMessage.getSender() != null &&
                    updatedMessage.getSender().getUserID() != null) {

                User sender = userService.getUserById(
                        updatedMessage.getSender().getUserID());

                if (sender != null) {
                    existingMessage.setSender(sender);
                }
            }

            if (updatedMessage.getReceiver() != null &&
                    updatedMessage.getReceiver().getUserID() != null) {

                User receiver = userService.getUserById(
                        updatedMessage.getReceiver().getUserID());

                if (receiver != null) {
                    existingMessage.setReceiver(receiver);
                }
            }

            return messageRepository.save(existingMessage);
        }

        throw new ResourceNotFoundException(
                "Message not found with ID: " + updatedMessage.getMessageID());
    }

    public boolean deleteMessageSafe(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        throw new ResourceNotFoundException("Message not found with ID: " + id);
    }

    public List<Message> getMessagesBySender(User sender) {
        return messageRepository.findBySender_UserID(sender.getUserID());
    }

    public List<Message> getMessagesByReceiver(User receiver) {
        return messageRepository.findByReceiver_UserID(receiver.getUserID());
    }

    public List<Message> getConversation(User u1, User u2) {
        return messageRepository
                .findBySender_UserIDAndReceiver_UserIDOrSender_UserIDAndReceiver_UserID(
                        u1.getUserID(), u2.getUserID(),
                        u2.getUserID(), u1.getUserID());
    }
}
package com.socialmedia.social_media_backend.service;

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

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageByIdSafe(Integer id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        return optionalMessage.orElse(null);
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

            if (updatedMessage.getSender() != null) {
                existingMessage.setSender(updatedMessage.getSender());
            }

            if (updatedMessage.getReceiver() != null) {
                existingMessage.setReceiver(updatedMessage.getReceiver());
            }

            return messageRepository.save(existingMessage);
        }

        throw new RuntimeException("Message not found with ID: " + updatedMessage.getMessageID());
    }

    public boolean deleteMessageSafe(Integer id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);

        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(id);
            return true;
        }

        return false;
    }

    // GET messages

    public List<Message> getMessagesBySender(User sender) {
        return messageRepository.findBySender(sender);
    }

    public List<Message> getMessagesByReceiver(User receiver) {
        return messageRepository.findByReceiver(receiver);
    }

    public List<Message> getConversation(User u1, User u2) {
        return messageRepository
                .findBySenderAndReceiverOrSenderAndReceiver(u1, u2, u2, u1);
    }
}
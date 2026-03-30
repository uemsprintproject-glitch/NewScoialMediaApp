package com.socialmedia.social_media_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.socialmedia.social_media_backend.model.Friend;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.FriendRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;

public class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendService friendService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFriends() {
        User user1 = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        User user2 = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);

        Friend f1 = Friend.builder().friendshipID(1).user1(user1).user2(user2).status(Friend.Status.pending).build();
        Friend f2 = Friend.builder().friendshipID(2).user1(user2).user2(user1).status(Friend.Status.accepted).build();

        when(friendRepository.findAll()).thenReturn(List.of(f1, f2));

        List<Friend> result = friendService.getAllFriends();
        assertEquals(2, result.size());
        verify(friendRepository).findAll();
    }

    @Test
    public void testGetFriendById() {
        User user1 = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        User user2 = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);
        Friend friendship = Friend.builder().friendshipID(10).user1(user1).user2(user2).status(Friend.Status.pending).build();

        when(friendRepository.findById(10)).thenReturn(Optional.of(friendship));

        Friend result = friendService.getFriendById(10);
        assertEquals(10, result.getFriendshipID());
        verify(friendRepository).findById(10);
    }

    @Test
    public void testGetPendingRequests() {
        User target = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);
        User requester = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);

        Friend pending = Friend.builder().friendshipID(1).user1(requester).user2(target).status(Friend.Status.pending).build();

        when(userRepository.findById(2)).thenReturn(Optional.of(target));
        when(friendRepository.findByUser2AndStatus(target, Friend.Status.pending)).thenReturn(List.of(pending));

        List<Friend> result = friendService.getPendingRequests(2);
        assertEquals(1, result.size());
        assertEquals(Friend.Status.pending, result.get(0).getStatus());
        verify(friendRepository).findByUser2AndStatus(target, Friend.Status.pending);
    }

    @Test
    public void testGetSentRequests() {
        User sender = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        User receiver = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);

        Friend pending = Friend.builder().friendshipID(1).user1(sender).user2(receiver).status(Friend.Status.pending).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(friendRepository.findByUser1AndStatus(sender, Friend.Status.pending)).thenReturn(List.of(pending));

        List<Friend> result = friendService.getSentRequests(1);
        assertEquals(1, result.size());
        verify(friendRepository).findByUser1AndStatus(sender, Friend.Status.pending);
    }

    @Test
    public void testSendFriendRequestCreatesNewPendingFriendship() {
        User user1 = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        User user2 = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.empty());
        when(friendRepository.findByUser2AndUser1(user2, user1)).thenReturn(Optional.empty());
        when(friendRepository.save(any(Friend.class))).thenAnswer(invocation -> {
            Friend saved = invocation.getArgument(0);
            saved.setFriendshipID(99);
            return saved;
        });

        Friend result = friendService.sendFriendRequest(1, 2);
        assertEquals(99, result.getFriendshipID());
        assertEquals(Friend.Status.pending, result.getStatus());
        verify(friendRepository).save(any(Friend.class));
    }

    @Test
    public void testSendFriendRequestReturnsExistingFriendship() {
        User user1 = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        User user2 = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);
        Friend existing = Friend.builder().friendshipID(7).user1(user1).user2(user2).status(Friend.Status.pending).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.of(existing));

        Friend result = friendService.sendFriendRequest(1, 2);
        assertEquals(7, result.getFriendshipID());
        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    public void testSendFriendRequestToSelfThrowsException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> friendService.sendFriendRequest(1, 1));
        assertEquals("Cannot send friend request to self", ex.getMessage());
    }

    @Test
    public void testAcceptFriendRequest() {
        User user1 = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        User user2 = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);
        Friend pending = Friend.builder().friendshipID(5).user1(user1).user2(user2).status(Friend.Status.pending).build();

        when(friendRepository.findById(5)).thenReturn(Optional.of(pending));
        when(friendRepository.save(pending)).thenReturn(pending);

        Friend result = friendService.acceptFriendRequest(5);
        assertEquals(Friend.Status.accepted, result.getStatus());
        verify(friendRepository).save(pending);
    }

    @Test
    public void testUnsendFriendRequest() {
        friendService.unsendFriendRequest(12);
        verify(friendRepository).deleteById(12);
    }

    @Test
    public void testRemoveFriend() {
        String result = friendService.removeFriend(21);
        assertEquals("Friend Removed", result);
        verify(friendRepository).deleteById(21);
    }

    @Test
    public void testAreFriendsTrueWhenReverseAcceptedExists() {
        User user1 = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        User user2 = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);
        Friend acceptedReverse = Friend.builder().friendshipID(15).user1(user2).user2(user1).status(Friend.Status.accepted).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.empty());
        when(friendRepository.findByUser2AndUser1(user2, user1)).thenReturn(Optional.of(acceptedReverse));

        Boolean result = friendService.areFriends(1, 2);
        assertTrue(result);
    }

    @Test
    public void testAreFriendsFalseWhenPendingExists() {
        User user1 = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        User user2 = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);
        Friend pending = Friend.builder().friendshipID(16).user1(user1).user2(user2).status(Friend.Status.pending).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.of(pending));

        Boolean result = friendService.areFriends(1, 2);
        assertFalse(result);
    }
}

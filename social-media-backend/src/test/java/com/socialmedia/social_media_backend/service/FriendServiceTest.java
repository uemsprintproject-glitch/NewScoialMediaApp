package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.exception.ResourceNotFoundException;
import com.socialmedia.social_media_backend.model.Friend;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.FriendRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendService friendService;

    private User user1;
    private User user2;
    private Friend pendingFriend;
    private Friend acceptedFriend;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = User.builder()
                .userID(1)
                .username("user1")
                .email("user1@test.com")
                .password("123")
                .build();

        user2 = User.builder()
                .userID(2)
                .username("user2")
                .email("user2@test.com")
                .password("123")
                .build();

        pendingFriend = Friend.builder()
                .friendshipID(10)
                .user1(user1)
                .user2(user2)
                .status(Friend.Status.pending)
                .build();

        acceptedFriend = Friend.builder()
                .friendshipID(11)
                .user1(user1)
                .user2(user2)
                .status(Friend.Status.accepted)
                .build();
    }

    @Test
    void testGetAllFriends() {
        when(friendRepository.findAll()).thenReturn(List.of(pendingFriend));

        List<Friend> result = friendService.getAllFriends();

        assertEquals(1, result.size());
        verify(friendRepository, times(1)).findAll();
    }

    @Test
    void testGetFriendById_Success() {
        when(friendRepository.findById(10)).thenReturn(Optional.of(pendingFriend));

        Friend result = friendService.getFriendById(10);

        assertEquals(10, result.getFriendshipID());
    }

    @Test
    void testGetFriendById_NotFound() {
        when(friendRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> friendService.getFriendById(99));
    }

    @Test
    void testGetPendingRequests() {
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser2AndStatus(user2, Friend.Status.pending))
                .thenReturn(List.of(pendingFriend));

        List<Friend> result = friendService.getPendingRequests(2);

        assertEquals(1, result.size());
        verify(friendRepository, times(1)).findByUser2AndStatus(user2, Friend.Status.pending);
    }

    @Test
    void testGetSentRequests() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(friendRepository.findByUser1AndStatus(user1, Friend.Status.pending))
                .thenReturn(List.of(pendingFriend));

        List<Friend> result = friendService.getSentRequests(1);

        assertEquals(1, result.size());
        verify(friendRepository, times(1)).findByUser1AndStatus(user1, Friend.Status.pending);
    }

    @Test
    void testSendFriendRequest_SelfNotAllowed() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> friendService.sendFriendRequest(1, 1));

        assertEquals("Cannot send friend request to self", ex.getMessage());
    }

    @Test
    void testSendFriendRequest_ReturnsExistingDirect() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.of(pendingFriend));

        Friend result = friendService.sendFriendRequest(1, 2);

        assertEquals(10, result.getFriendshipID());
        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    void testSendFriendRequest_ReturnsExistingReverse() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.empty());
        when(friendRepository.findByUser2AndUser1(user2, user1)).thenReturn(Optional.of(pendingFriend));

        Friend result = friendService.sendFriendRequest(1, 2);

        assertEquals(10, result.getFriendshipID());
        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    void testSendFriendRequest_CreatesNewPending() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.empty());
        when(friendRepository.findByUser2AndUser1(user2, user1)).thenReturn(Optional.empty());
        when(friendRepository.save(any(Friend.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Friend result = friendService.sendFriendRequest(1, 2);

        assertEquals(Friend.Status.pending, result.getStatus());
        assertEquals(user1, result.getUser1());
        assertEquals(user2, result.getUser2());
        verify(friendRepository, times(1)).save(any(Friend.class));
    }

    @Test
    void testSendFriendRequest_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> friendService.sendFriendRequest(1, 2));
    }

    @Test
    void testAcceptFriendRequest() {
        when(friendRepository.findById(10)).thenReturn(Optional.of(pendingFriend));
        when(friendRepository.save(any(Friend.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Friend result = friendService.acceptFriendRequest(10);

        assertEquals(Friend.Status.accepted, result.getStatus());
        verify(friendRepository, times(1)).save(pendingFriend);
    }

    @Test
    void testAcceptFriendRequest_NotFound() {
        when(friendRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> friendService.acceptFriendRequest(10));
    }

    @Test
    void testUnsendFriendRequest() {
        friendService.unsendFriendRequest(10);

        verify(friendRepository, times(1)).deleteById(10);
    }

    @Test
    void testRemoveFriend() {
        String result = friendService.removeFriend(10);

        assertEquals("Friend Removed", result);
        verify(friendRepository, times(1)).deleteById(10);
    }

    @Test
    void testAreFriends_TrueForAccepted() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.of(acceptedFriend));

        Boolean result = friendService.areFriends(1, 2);

        assertTrue(result);
    }

    @Test
    void testAreFriends_FalseForPending() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.of(pendingFriend));

        Boolean result = friendService.areFriends(1, 2);

        assertFalse(result);
    }

    @Test
    void testAreFriends_UsesReverseLookup() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.empty());
        when(friendRepository.findByUser2AndUser1(user2, user1)).thenReturn(Optional.of(acceptedFriend));

        Boolean result = friendService.areFriends(1, 2);

        assertTrue(result);
    }

    @Test
    void testAreFriends_NoRelation() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(friendRepository.findByUser1AndUser2(user1, user2)).thenReturn(Optional.empty());
        when(friendRepository.findByUser2AndUser1(user2, user1)).thenReturn(Optional.empty());

        Boolean result = friendService.areFriends(1, 2);

        assertFalse(result);
    }
}
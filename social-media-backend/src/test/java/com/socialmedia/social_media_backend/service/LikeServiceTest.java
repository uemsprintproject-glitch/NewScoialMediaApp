package com.socialmedia.social_media_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.socialmedia.social_media_backend.model.Like;
import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.LikeRepository;

public class LikeServiceTest {

    @Mock
    private LikeRepository repo;

    @InjectMocks
    private LikeService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllLikes() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Post post = new Post();
        post.setPostID(1);

        Like l1 = new Like(1, Timestamp.valueOf("2026-03-29 10:00:00"), post, user);
        Like l2 = new Like(2, Timestamp.valueOf("2026-03-29 10:05:00"), post, user);

        when(repo.findAll()).thenReturn(List.of(l1, l2));

        List<Like> result = service.getAllLikes();
        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    public void testGetLikeById() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Post post = new Post();
        post.setPostID(1);
        Like like = new Like(1, Timestamp.valueOf("2026-03-29 10:00:00"), post, user);

        when(repo.findById(1)).thenReturn(Optional.of(like));

        Like result = service.getLikeById(1);
        assertEquals(1, result.getLikeID());
        verify(repo).findById(1);
    }

    @Test
    public void testGetLikesByUser() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Post post = new Post();
        post.setPostID(1);

        Like l1 = new Like(1, Timestamp.valueOf("2026-03-29 10:00:00"), post, user);
        Like l2 = new Like(2, Timestamp.valueOf("2026-03-29 10:05:00"), post, user);

        when(repo.findByUserUserID(1)).thenReturn(List.of(l1, l2));

        List<Like> result = service.getLikesByUser(1);
        assertEquals(2, result.size());
        verify(repo).findByUserUserID(1);
    }

    @Test
    public void testGetLikesByPost() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Post post = new Post();
        post.setPostID(1);

        Like l1 = new Like(1, Timestamp.valueOf("2026-03-29 10:00:00"), post, user);
        Like l2 = new Like(2, Timestamp.valueOf("2026-03-29 10:05:00"), post, user);

        when(repo.findByPostPostID(1)).thenReturn(List.of(l1, l2));

        List<Like> result = service.getLikesByPost(1);
        assertEquals(2, result.size());
        verify(repo).findByPostPostID(1);
    }

    @Test
    public void testCreateLike() {
        User user = new User(1, "soumyojit", "soumyojit@gmail.com", "123", null, null, null);
        Post post = new Post();
        post.setPostID(1);

        Like like = new Like(100, Timestamp.valueOf("2026-03-29 09:00:00"), post, user);

        when(repo.save(any(Like.class))).thenAnswer(invocation -> {
            Like saved = invocation.getArgument(0);
            saved.setLikeID(1);
            return saved;
        });

        Like result = service.createLike(like);
        assertEquals(1, result.getLikeID());
        assertNotNull(result.getTimestamp());
        verify(repo).save(any(Like.class));
    }

    @Test
    public void testUpdateLike() {
        User oldUser = new User(1, "old", "old@gmail.com", "123", null, null, null);
        User newUser = new User(2, "new", "new@gmail.com", "456", null, null, null);

        Post oldPost = new Post();
        oldPost.setPostID(1);
        Post newPost = new Post();
        newPost.setPostID(2);

        Like existing = new Like(1, Timestamp.valueOf("2026-03-29 09:00:00"), oldPost, oldUser);
        Like updated = new Like(1, null, newPost, newUser);

        when(repo.findById(1)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Like result = service.updateLike(updated);
        assertEquals(2, result.getUser().getUserID());
        assertEquals(2, result.getPost().getPostID());
        assertNotNull(result.getTimestamp());
        verify(repo).findById(1);
        verify(repo).save(existing);
    }

    @Test
    public void testDeleteLike() {
        service.deleteLike(1);
        verify(repo).deleteById(1);
    }
}
package com.socialmedia.social_media_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.UserRepository;

public class UserServiceTest {
    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers(){
        User u1 = new User(1, "anishka", "anishka@gmail.com", "123", null, null, null);
        User u2 = new User(2, "sagnik", "sagnik@gmail.com", "234", null, null, null);

        when(repo.findAll()).thenReturn(List.of(u1,u2));

        List<User> result = service.getAllUsers();
        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    public void testGetUserById(){
        User u = new User(1, "anishka", "anishka@gmail.com", "123", null, null, null);
        when(repo.findById(1)).thenReturn(Optional.of(u));
        User result = service.getUserById(1);
        assertEquals(result.getUsername(), "anishka");
    }

    @Test
    public void testGetUserByUsername(){
        User u1 = new User(1, "anishka", "anishka@gmail.com", "123", null, null, null);
        User u2 = new User(2, "anishka", "anih@gmail.com", "453", null, null, null);
        when(repo.findByUsername("anishka")).thenReturn(List.of(u1,u2));
        List<User> result = service.getUserByUsername("anishka");
        assertEquals(2, result.size());
    }

    @Test
    public void testGetUserByEmail(){
        User user = new User(1, "anishka", "anishka@gmail.com", "123", null, null, null);
        when(repo.findByEmail("anishka@gmail.com")).thenReturn(user);
        User result = service.getUserByEmail("anishka@gmail.com");
        assertEquals("anishka",result.getUsername());
    }

    @Test
    public void testCreateUser(){
        User user = new User(null, "Anishka", "anishka@gmail.com", "123", null, null, null);
        when(repo.save(user)).thenReturn(new User(1, "Anishka", "anishka@gmail.com", "123", null, null, null));
        User result = service.createUser(user);
        assertEquals(1, result.getUserID());
    }

    @Test
    public void testUpdateUser(){
        User existingUser = new User(1, "Anishka", "anishka@gmail.com", "123", null, null, null);
        User updatedUser = new User(1, "Anishka", "anishka@gmail.com", "456", null, null, null);

        when(repo.findById(1)).thenReturn(Optional.of(existingUser));
        when(repo.save(existingUser)).thenReturn(updatedUser);
        User result = service.updateUser(updatedUser);
        assertEquals("456", result.getPassword());
    }

    @Test
    public void testDeleteUser(){
        service.deleteUser(1);
        verify(repo).deleteById(1);
    }
}

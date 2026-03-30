package com.socialmedia.social_media_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.socialmedia.social_media_backend.model.Group;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.GroupRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;

public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private GroupService groupService;

    private User admin;
    private Group group1;
    private Group group2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        admin = new User(1, "adminUser", "admin@test.com", "password", null, null, null);
        group1 = new Group(1, "Test Group 1", admin);
        group2 = new Group(2, "Test Group 2", admin);
    }

    @Test
    void testGetAllGroups() {
        when(groupRepo.findAll()).thenReturn(Arrays.asList(group1, group2));

        List<Group> result = groupService.getAllGroups();

        assertEquals(2, result.size());
        verify(groupRepo, times(1)).findAll();
    }

    @Test
    void testGetGroupById_Success() {
        when(groupRepo.findById(1)).thenReturn(Optional.of(group1));

        Group result = groupService.getGroupById(1);

        assertNotNull(result);
        assertEquals("Test Group 1", result.getGroupName());
        verify(groupRepo, times(1)).findById(1);
    }

    @Test
    void testGetGroupById_NotFound() {
        when(groupRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> groupService.getGroupById(99));
        verify(groupRepo, times(1)).findById(99);
    }

    @Test
    void testGetGroupByName() {
        when(groupRepo.findByGroupNameContainingIgnoreCase("Test")).thenReturn(Arrays.asList(group1, group2));

        List<Group> result = groupService.getGroupByName("Test");

        assertEquals(2, result.size());
        verify(groupRepo, times(1)).findByGroupNameContainingIgnoreCase("Test");
    }

    @Test
    void testCreateGroup_Success() {
        Group newGroup = new Group(null, "New Group", admin);
        when(userRepo.findById(1)).thenReturn(Optional.of(admin));
        when(groupRepo.findByGroupNameIgnoreCase("New Group")).thenReturn(Optional.empty());
        when(groupRepo.save(any(Group.class))).thenReturn(newGroup);

        groupService.createGroup(newGroup);

        verify(userRepo, times(1)).findById(1);
        verify(groupRepo, times(1)).findByGroupNameIgnoreCase("New Group");
        verify(groupRepo, times(1)).save(newGroup);
    }

    @Test
    void testCreateGroup_UserNotFound() {
        Group newGroup = new Group(null, "New Group", admin);
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> groupService.createGroup(newGroup));
        verify(userRepo, times(1)).findById(1);
    }

    @Test
    void testCreateGroup_GroupNameExists() {
        Group newGroup = new Group(null, "Test Group 1", admin);
        when(userRepo.findById(1)).thenReturn(Optional.of(admin));
        when(groupRepo.findByGroupNameIgnoreCase("Test Group 1")).thenReturn(Optional.of(group1));

        assertThrows(RuntimeException.class, () -> groupService.createGroup(newGroup));
        verify(groupRepo, times(1)).findByGroupNameIgnoreCase("Test Group 1");
    }

    @Test
    void testUpdateGroup_Success() {
        Group updatedGroup = new Group(1, "Updated Name", admin);
        when(groupRepo.findById(1)).thenReturn(Optional.of(group1));
        when(userRepo.findById(1)).thenReturn(Optional.of(admin));
        when(groupRepo.save(any(Group.class))).thenReturn(updatedGroup);

        Group result = groupService.updateGroup(updatedGroup);

        assertNotNull(result);
        assertEquals("Updated Name", result.getGroupName());
        verify(groupRepo, times(1)).findById(1);
        verify(groupRepo, times(1)).save(any(Group.class));
    }

    @Test
    void testGetGroupsByAdmin_Success() {
        when(userRepo.findById(1)).thenReturn(Optional.of(admin));
        when(groupRepo.findByAdmin(admin)).thenReturn(Arrays.asList(group1, group2));

        List<Group> result = groupService.getGroupsByAdmin(1);

        assertEquals(2, result.size());
        verify(userRepo, times(1)).findById(1);
        verify(groupRepo, times(1)).findByAdmin(admin);
    }

    @Test
    void testGetGroupsByAdmin_UserNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> groupService.getGroupsByAdmin(1));
        verify(userRepo, times(1)).findById(1);
    }
}

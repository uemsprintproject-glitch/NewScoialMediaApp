package com.socialmedia.social_media_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.socialmedia.social_media_backend.model.Group;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.GroupRepository;
import com.socialmedia.social_media_backend.service.GroupService;
import com.socialmedia.social_media_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

public class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService service;

    @Mock
    private GroupRepository groupRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private GroupController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllGroups() throws Exception {
        when(service.getAllGroups()).thenReturn(List.of());

        mockMvc.perform(get("/member/groups/all"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetGroupById() throws Exception {
        Group group = new Group();
        group.setGroupID(1);

        when(service.getGroupById(1)).thenReturn(group);

        mockMvc.perform(get("/member/groups/by-id?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetGroupByName() throws Exception {
        Group group = new Group();
        group.setGroupName("Developers");

        when(service.getGroupByName("Developers")).thenReturn(List.of(group));

        mockMvc.perform(get("/member/groups/by-name?groupName=Developers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetGroupsByAdmin() throws Exception {
        User admin = new User();
        admin.setUserID(1);

        Group group = new Group();
        group.setGroupID(1);
        group.setAdmin(admin);

        when(service.getGroupsByAdmin(1)).thenReturn(List.of(group));

        mockMvc.perform(get("/member/groups/by-admin?adminId=1"))
                .andExpect(status().isOk());
    }
}
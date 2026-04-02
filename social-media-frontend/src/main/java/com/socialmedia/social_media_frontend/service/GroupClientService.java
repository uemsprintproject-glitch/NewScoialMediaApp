package com.socialmedia.social_media_frontend.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.socialmedia.social_media_frontend.model.Group;

@Service
public class GroupClientService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8090/member/groups";

    public List<Group> getAllGroups() {
        Group[] groups = restTemplate.getForObject(BASE_URL + "/all", Group[].class);
        return Arrays.asList(groups);
    }

    public Group getGroupById(int id) {
        return restTemplate.getForObject(BASE_URL + "/by-id?id=" + id, Group.class);
    }

    public List<Group> getGroupByName(String groupName) {
        Group[] groups = restTemplate.getForObject(BASE_URL + "/by-name?groupName=" + groupName, Group[].class);
        return Arrays.asList(groups);
    }

    public void createGroup(Group group) {
        restTemplate.postForObject(BASE_URL + "/create", group, String.class);
    }

    public void updateGroup(Group group) {
        restTemplate.put(BASE_URL + "/update", group);
    }

    public List<Group> getGroupsByAdmin(int adminId) {
        Group[] groups = restTemplate.getForObject(BASE_URL + "/by-admin?adminId=" + adminId, Group[].class);
        return Arrays.asList(groups);
    }
}

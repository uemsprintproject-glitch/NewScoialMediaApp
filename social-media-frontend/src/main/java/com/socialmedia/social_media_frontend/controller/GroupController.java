package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Group;
import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.GroupClientService;
import com.socialmedia.social_media_frontend.util.PaginationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member/groups")
public class GroupController {

    private final GroupClientService service;

    public GroupController(GroupClientService service) {
        this.service = service;
    }

    @GetMapping
    public String page() {
        return "groups/groups";
    }

    @GetMapping("/all")
    public String getAllGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getAllGroups(),
                page,
                size,
                "/member/groups/all",
                "groups/result",
                model,
                Map.of());
    }

    @GetMapping("/by-id")
    public String getGroupById(
            @RequestParam int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Group group = service.getGroupById(id);
        if (group == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found with ID: " + id);
        }
        return PaginationUtils.renderPaginatedResult(
                List.of(group),
                page,
                size,
                "/member/groups/by-id",
                "groups/result",
                model,
                Map.of("id", id));
    }

    @GetMapping("/by-name")
    public String getGroupByName(
            @RequestParam String groupName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        List<Group> groups = service.getGroupByName(groupName);
        if (groups == null || groups.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No groups found with name: " + groupName);
        }
        return PaginationUtils.renderPaginatedResult(
                groups,
                page,
                size,
                "/member/groups/by-name",
                "groups/result",
                model,
                Map.of("groupName", groupName));
    }

    @GetMapping("/by-id-form")
    public String getByIdForm() {
        return "groups/get-by-id";
    }

    @GetMapping("/by-name-form")
    public String getByNameForm() {
        return "groups/get-by-name";
    }

    @GetMapping("/by-admin-form")
    public String getByAdminForm() {
        return "groups/get-by-admin";
    }

    @GetMapping("/create-form")
    public String getCreateForm() {
        return "groups/create-group";
    }

    @GetMapping("/update-form")
    public String getUpdateForm() {
        return "groups/update-group";
    }

    @PutMapping("/create")
    public String createGroup(@RequestParam String groupName, @RequestParam int adminID) {
        Group group = new Group();
        group.setGroupName(groupName);
        User admin = new User();
        admin.setUserID(adminID);
        group.setAdmin(admin);
        service.createGroup(group);
        return "redirect:/member/groups";
    }

    @PostMapping("/update")
    public String updateGroup(@RequestParam int groupID, @RequestParam String groupName, @RequestParam int adminID) {
        Group group = new Group();
        group.setGroupID(groupID);
        group.setGroupName(groupName);
        User admin = new User();
        admin.setUserID(adminID);
        group.setAdmin(admin);
        service.updateGroup(group);
        return "redirect:/member/groups";
    }

    @GetMapping("/by-admin")
    public String getGroupsByAdmin(
            @RequestParam int adminId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        return PaginationUtils.renderPaginatedResult(
                service.getGroupsByAdmin(adminId),
                page,
                size,
                "/member/groups/by-admin",
                "groups/result",
                model,
                Map.of("adminId", adminId));
    }
}

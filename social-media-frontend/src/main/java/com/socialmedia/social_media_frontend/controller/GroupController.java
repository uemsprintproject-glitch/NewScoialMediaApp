package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Group;
import com.socialmedia.social_media_frontend.model.User;
import com.socialmedia.social_media_frontend.service.GroupClientService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public String getAllGroups(Model model) {
        model.addAttribute("data", service.getAllGroups());
        return "groups/result";
    }

    @GetMapping("/by-id")
    public String getGroupById(@RequestParam int id, Model model) {
        Group group = service.getGroupById(id);
        if (group == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found with ID: " + id);
        }
        model.addAttribute("data", java.util.Collections.singletonList(group));
        return "groups/result";
    }

    @GetMapping("/by-name")
    public String getGroupByName(@RequestParam String groupName, Model model) {
        List<Group> groups = service.getGroupByName(groupName);
        if (groups == null || groups.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No groups found with name: " + groupName);
        }
        model.addAttribute("data", groups);
        return "groups/result";
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

    @PostMapping("/create")
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
    public String getGroupsByAdmin(@RequestParam int adminId, Model model) {
        model.addAttribute("data", service.getGroupsByAdmin(adminId));
        return "groups/result";
    }
}

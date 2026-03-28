package com.socialmedia.social_media_frontend.controller;

import com.socialmedia.social_media_frontend.model.Group;
import com.socialmedia.social_media_frontend.service.GroupClientService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("data", List.of(service.getGroupById(id)));
        return "groups/result";
    }

    @PostMapping("/create")
    public String createGroup(@ModelAttribute Group group) {
        service.createGroup(group);
        return "redirect:/member/groups";
    }

    @PostMapping("/update")
    public String updateGroup(@ModelAttribute Group group) {
        service.updateGroup(group);
        return "redirect:/member/groups";
    }

    @GetMapping("/by-admin")
    public String getGroupsByAdmin(@RequestParam int adminId, Model model) {
        model.addAttribute("data", service.getGroupsByAdmin(adminId));
        return "groups/result";
    }

    @GetMapping("/by-name")
    public String getGroupByName(@RequestParam String name, Model model) {
        model.addAttribute("data", service.getGroupByName(name));
        return "groups/result";
    }
}

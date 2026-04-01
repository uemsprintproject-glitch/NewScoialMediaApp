package com.socialmedia.social_media_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialmedia.social_media_backend.repository.GroupRepository;
import com.socialmedia.social_media_backend.service.GroupService;
import com.socialmedia.social_media_backend.service.UserService;
import com.socialmedia.social_media_backend.model.Group;
import com.socialmedia.social_media_backend.model.User;

@RestController
@RequestMapping("/member/groups")
@CrossOrigin(origins = "http://localhost:8080")
public class GroupController {
	
	@Autowired
    GroupService service;
	
	@Autowired
    GroupRepository groupRepo;
	
	@Autowired
	UserService userService;
    
	
	
    @GetMapping("/all")
    public List<Group> getAllGroups() {
        return service.getAllGroups();
    }

    @GetMapping("/by-id")
    public Group getGroupById(@RequestParam int id) {
        return service.getGroupById(id);
    }

    @GetMapping("/by-name")
    public List<Group> getGroupByName(@RequestParam String groupName) {
        return service.getGroupByName(groupName);
    }

    @GetMapping("/by-admin")
    public List<Group> getGroupsByAdmin(@RequestParam("adminId") int adminId) {
        return service.getGroupsByAdmin(adminId);
    }

    @PostMapping("/create")
    public String createGroup(@RequestBody Group group) {
        service.createGroup(group);
        return "Created";
    }
    
    @PostMapping("/update")
    public Group updateGroup(@RequestBody Group group) {
    	User user = userService.getUserById(group.getAdmin().getUserID());
    	System.out.println(group);
    	group.setAdmin(user);
    	System.out.println(group.getGroupID());
        return service.updateGroup(group);
    }

//    @DeleteMapping("/delete/{id}")
//    public String deleteGroup(@PathVariable int id) {
//    	if (!groupRepo.existsById(id)) {
//			return "Group not found";
//		}
//        service.deleteGroup(id);
//        return "Group deleted successfully";
//    }

}

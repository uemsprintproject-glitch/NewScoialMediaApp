package com.socialmedia.social_media_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.socialmedia.social_media_backend.exception.ResourceNotFoundException;
import com.socialmedia.social_media_backend.model.Group;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.GroupRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;

@Service
public class GroupService {

	private final GroupRepository groupRepo;
	private final UserRepository userRepo;

	public GroupService(GroupRepository groupRepo, UserRepository userRepo) {
		this.groupRepo = groupRepo;
		this.userRepo = userRepo;
	}

	// Get all groups
	public List<Group> getAllGroups() {
		return groupRepo.findAll();
	}

	// Get group by ID
	public Group getGroupById(int id) {
		return groupRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Group not found with id " + id));
	}

	public List<Group> getGroupByName(String groupName) {
		return groupRepo.findByGroupNameContainingIgnoreCase(groupName);
	}

	public void createGroup(Group group) {
		int userId = group.getAdmin().getUserID();

		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

		if (groupRepo.findByGroupNameIgnoreCase(group.getGroupName()).isPresent()) {
			throw new RuntimeException("Group name '" + group.getGroupName() + "' already exists");
		}

		group.setAdmin(user);
		groupRepo.save(group);
	}

	public Group updateGroup(Group updatedGroup) {
		Group existingGroup = groupRepo.findById(updatedGroup.getGroupID())
				.orElseThrow(() -> new ResourceNotFoundException("Group not found"));

		existingGroup.setGroupName(updatedGroup.getGroupName());

		if (updatedGroup.getAdmin() != null) {
			int adminId = updatedGroup.getAdmin().getUserID();
			User admin = userRepo.findById(adminId)
					.orElseThrow(() -> new ResourceNotFoundException("Admin user not found with id " + adminId));
			existingGroup.setAdmin(admin);
		}
		return groupRepo.save(existingGroup);
	}

	// public void deleteGroup(int id) {
	// if (!groupRepo.existsById(id)) {
	// throw new RuntimeException("Group not found with id " + id);
	// }
	// groupRepo.deleteById(id);
	// }

	public List<Group> getGroupsByAdmin(int adminId) {
		User admin = userRepo.findById(adminId)
				.orElseThrow(() -> new ResourceNotFoundException("Admin user not found with id " + adminId));

		return groupRepo.findByAdmin(admin);
	}
}

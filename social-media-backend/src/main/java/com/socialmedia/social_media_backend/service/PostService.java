package com.socialmedia.social_media_backend.service;

import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.PostRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post getPostById(Integer postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    public Post createPost(Integer userId, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = Post.builder()
                .content(content)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .user(user)
                .build();

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByTimestampDesc();
    }

    public List<Post> getPostsByUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getPosts();
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    public Post updatePost(Post updatedPost) {
        Post existingPost = postRepository.findById(updatedPost.getPostID())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        existingPost.setContent(updatedPost.getContent());

        return postRepository.save(existingPost);
    }

}
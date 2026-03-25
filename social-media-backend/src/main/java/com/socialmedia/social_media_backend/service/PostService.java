package com.socialmedia.social_media_backend.service;
import com.socialmedia.social_media_backend.model.Post;
import com.socialmedia.social_media_backend.model.User;
import com.socialmedia.social_media_backend.repository.PostRepository;
import com.socialmedia.social_media_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createPost(Integer userId, String content) {

        User user = userRepository.findById(userId).orElse(null);

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
        User user = userRepository.findById(userId).orElseThrow();
        return postRepository.findByUser(user);
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }
}
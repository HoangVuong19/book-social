package com.example.post.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.post.dto.request.PostRequest;
import com.example.post.dto.response.PostResponse;
import com.example.post.entity.Post;
import com.example.post.mapper.PostMapper;
import com.example.post.repository.PostRepository;
import com.example.post.utils.UserContext;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserContext userContext;

    public PostResponse createPost(PostRequest request) {
        String userId = userContext.getCurrentUsername();

        Post post = Post.builder()
                .content(request.content())
                .userId(userId)
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();

        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    public List<PostResponse> getMyPosts() {
        String userId = userContext.getCurrentUsername();

        return postRepository.findAllByUserId(userId).stream()
                .map(postMapper::toPostResponse)
                .toList();
    }
}

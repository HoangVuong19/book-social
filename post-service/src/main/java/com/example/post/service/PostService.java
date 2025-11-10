package com.example.post.service;

import java.time.Instant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.post.config.serialize.PageResponse;
import com.example.post.dto.request.PostRequest;
import com.example.post.dto.response.PostResponse;
import com.example.post.dto.response.UserProfileResponse;
import com.example.post.entity.Post;
import com.example.post.mapper.PostMapper;
import com.example.post.repository.PostRepository;
import com.example.post.repository.httpclient.ProfileClient;
import com.example.post.utils.DateTimeFormatter;
import com.example.post.utils.UserContext;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    UserContext userContext;
    DateTimeFormatter dateTimeFormatter;
    ProfileClient profileClient;

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

    public PageResponse<PostResponse> getMyPosts(int page, int size) {
        String userId = userContext.getCurrentUsername();

        UserProfileResponse userProfile = null;

        try {
            userProfile = profileClient.getProfile(userId).getData();
        } catch (Exception e) {
            log.error("Error while getting user profile", e);
        }
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = postRepository.findAllByUserId(userId, pageable);

        String username = userProfile != null ? userProfile.username() : null;
        var postList = pageData.getContent().stream()
                .map(post -> {
                    String formattedCreated = dateTimeFormatter.format(post.getCreatedDate());
                    return new PostResponse(
                            post.getId(),
                            post.getContent(),
                            post.getUserId(),
                            username,
                            formattedCreated,
                            post.getCreatedDate(),
                            post.getModifiedDate());
                })
                .toList();

        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .build();
    }
}

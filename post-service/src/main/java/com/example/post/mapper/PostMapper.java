package com.example.post.mapper;

import org.mapstruct.Mapper;

import com.example.post.dto.response.PostResponse;
import com.example.post.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}

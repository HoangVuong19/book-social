package com.example.post.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.post.entity.Post;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByUserId(String userId);
}

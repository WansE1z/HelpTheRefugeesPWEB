package com.example.pwebproject.service;

import com.example.pwebproject.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    List<Post> findAll();
    Post save(Post post);
    Page<Post> findPaginated(Pageable pageable);
    Post findById(Long postId);
    void deleteById(Long postId);
}
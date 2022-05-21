package com.example.pwebproject.service.impl;

import com.example.pwebproject.exception.NotFoundException;
import com.example.pwebproject.model.Post;
import com.example.pwebproject.repository.PostRepository;
import com.example.pwebproject.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> findAll() {
        List<Post> posts = new LinkedList<>();
        postRepository.findAll().iterator().forEachRemaining(posts::add);
        return posts;
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Page<Post> findPaginated(Pageable pageable) {
        List<Post> posts = findAll();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Post> list;
        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex);
        }
        Page<Post> PostPage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), posts.size());
        return PostPage;
    }

    @Override
    public Post findById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(!postOptional.isPresent()) {
            throw new NotFoundException("Post with id " + postId + "not found!");
        }
        return postOptional.get();
    }

    @Override
    public void deleteById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(!post.isPresent()) {
            throw new NotFoundException("Post with id: " + postId + "not found");
        }
        postRepository.deleteById(postId);
    }
}

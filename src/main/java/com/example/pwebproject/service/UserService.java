package com.example.pwebproject.service;

import com.example.pwebproject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> findAll();
    void save(User user);
    void saveWithoutHash(User user);
    User findByUsername(String username);
    User getAuthenticatedUser();
    Page<User> findPaginated(Pageable pageable);

    User findById(Long userId);
    void deleteById(Long userId);
}

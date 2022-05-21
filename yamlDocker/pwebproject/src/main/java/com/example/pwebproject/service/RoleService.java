package com.example.pwebproject.service;

import com.example.pwebproject.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAllRoles();
    Optional<Role> findById(Long id);
}

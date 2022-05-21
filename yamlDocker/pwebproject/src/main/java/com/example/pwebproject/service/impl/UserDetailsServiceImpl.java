package com.example.pwebproject.service.impl;

import com.example.pwebproject.model.Role;
import com.example.pwebproject.model.User;
import com.example.pwebproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(!user.isPresent()) {
            throw new UsernameNotFoundException(username);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Role role : user.get().getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }

        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), grantedAuthorities);
    }
}

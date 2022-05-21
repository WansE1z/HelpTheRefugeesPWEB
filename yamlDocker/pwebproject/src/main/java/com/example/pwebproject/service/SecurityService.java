package com.example.pwebproject.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
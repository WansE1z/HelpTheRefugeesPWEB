package com.example.pwebproject.service;

import com.example.pwebproject.model.Post;
import com.example.pwebproject.model.UserReservation;

public interface EmailService {
    void sendEmail(UserReservation userReservation, Post post, UserService userService);
}

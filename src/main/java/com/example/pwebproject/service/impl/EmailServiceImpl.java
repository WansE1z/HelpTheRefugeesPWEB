package com.example.pwebproject.service.impl;

import com.example.pwebproject.email.DefaultEmailService;
import com.example.pwebproject.model.Post;
import com.example.pwebproject.model.UserReservation;
import com.example.pwebproject.service.EmailService;
import com.example.pwebproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import javax.persistence.Access;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final DefaultEmailService defaultEmailService;
    @Override
    public void sendEmail(UserReservation userReservation, Post post, UserService userService) {
        try {
            String email = "Hello!\nYou have just made a reservation for " + userReservation.getTotalRes() + " people. \nAddress of the reservation is " + post.getAddress() + ". \nMobile number for the reservation is "  + post.getPhoneNo() + ". For futher informations do not hesitate to contact the property.\nThank you and best regards!";
            defaultEmailService.sendSimpleEmail(userService.getAuthenticatedUser().getEmail(),
                    "New reservation",
                    email);
        } catch (MailException mailException) {
            System.out.println("Error while sending out email..{}" + mailException.getStackTrace());
        }
    }
}

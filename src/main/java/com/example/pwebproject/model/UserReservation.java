package com.example.pwebproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="user_reservation")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_reservation_id")
    private Long userReservationId;

    @OneToOne(mappedBy = "reservation")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @NotNull(message = "Number of people cannot be empty!")
    @Min(value = 1, message = "Number of people cannot be empty!")
    private Long totalRes;

    @NotNull
    @NotEmpty(message = "Phone number cannot be empty!")
    public String phoneNumber;
}

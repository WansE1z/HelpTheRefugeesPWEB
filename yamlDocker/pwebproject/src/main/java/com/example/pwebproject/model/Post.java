package com.example.pwebproject.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @NotEmpty(message = "Title cannot be empty!")
    @NotNull
    private String title;

    @NotEmpty(message = "Post content cannot be empty!")
    @NotNull
    private String content;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Picture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<UserReservation> reservations = new ArrayList<>();

    @NotNull
    @Min(value = 1, message = "Maximum number of people must be bigger than 1!")
    private Long noTotal;

    private Long noAvailablePlaces;

    @NotEmpty(message = "Address cannot be empty!")
    @NotNull
    private String address;

    @NotEmpty(message = "Phone number cannot be empty!")
    @NotNull
    private String phoneNo;


}

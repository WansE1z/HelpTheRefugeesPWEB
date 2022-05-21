package com.example.pwebproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotEmpty(message = "Username cannot be empty!")
    @Column(unique = true)
    private String username;

    @Email(message = "Email is not valid!")
    @NotEmpty(message = "Email cannot be empty!")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password cannot be empty!")
    @Length(min = 4, message = "Password length must be greater than 4!")
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_date")
    @Past(message = "BirthDate must be in the past!")
    private LocalDate birthDate;

    @Column(name = "last_name")
    @NotEmpty(message = "Last name cannot be empty!")
    private String lastName;

    @Column(name = "first_name")
    @NotEmpty(message = "First name cannot be empty!")
    private String firstName;

    private String picture;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;


    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Post> postedPosts;


    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user_reservation_id")
    @ToString.Exclude
    private UserReservation reservation;

    public String getUserRoles(){
        String string = "";
        for(Role r : roles)
            string = string + r.getName() + " ";
        return string;
    }

}

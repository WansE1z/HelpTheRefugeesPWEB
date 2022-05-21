package com.example.pwebproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(columnDefinition = "enum('ROLE_ADMIN', 'ROLE_USER')")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

}

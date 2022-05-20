package com.example.pwebproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "picture")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_id")
    private Long pictureId;

    @Column(name = "picture")
    @Lob
    private Byte[] picture;

    @ManyToOne
    @JoinColumn(name = "Post_id", nullable = false)
    private Post post;
}

package com.company.telegrambot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "professors")
@NoArgsConstructor
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "full_name",nullable = false)
    private String fullName;


    @Column(name = "background",nullable = false)
    private String background;

    @Column(name = "linkedin_account",nullable = false)
    private String linkedInAccount;

    @Column(name = "email",nullable = false)
    private String email;

    public Professor(String fullName, String background, String linkedInAccount, String email) {
        this.fullName = fullName;
        this.background = background;
        this.linkedInAccount = linkedInAccount;
        this.email = email;
    }
}

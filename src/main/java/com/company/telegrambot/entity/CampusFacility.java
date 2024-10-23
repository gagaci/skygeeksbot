package com.company.telegrambot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "campus_facilities")
public class CampusFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "contact", nullable = false)
    private String contact;
}

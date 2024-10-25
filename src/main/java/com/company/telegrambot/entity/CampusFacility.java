package com.company.telegrambot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "campus_facilities")
@NoArgsConstructor
public class CampusFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;


    @Column(name = "facility", nullable = false)
    private String facility;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "contact", nullable = false)
    private String contact;

    public CampusFacility(String facility, String location, String contact) {
        this.facility = facility;
        this.location = location;
        this.contact = contact;
    }
}

package com.company.telegrambot.entity;

import com.company.telegrambot.enums.ClubType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "clubs")
@NoArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "club_type", nullable = false)
    private ClubType clubType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "founder", nullable = false)
    private String founder;

    @Column(name = "founder_contanct", nullable = false)
    private String founderContact;

    @Column(name = "contact", nullable = false)
    private String contact;

    public Club(String name, ClubType clubType, String description, String founder, String founderContact, String contact) {
        this.name = name;
        this.clubType = clubType;
        this.description = description;
        this.founder = founder;
        this.founderContact = founderContact;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}

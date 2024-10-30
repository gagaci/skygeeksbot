package com.company.telegrambot.entity;

import com.company.telegrambot.enums.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private Long chatId;

    @Column(name = "firtname",nullable = false)
    private String firstName;

    @Column(name = "state")
    private State state = State.NEW;

    private LocalDateTime registeredAt;



    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}

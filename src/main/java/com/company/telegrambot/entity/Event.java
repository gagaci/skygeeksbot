package com.company.telegrambot.entity;

import com.company.telegrambot.enums.EventType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "events")
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "event_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventType eventType;

    @Column(name = "venue",nullable = false)
    private String venue;

    @Column(name = "date",nullable = false)
    private LocalDate date;

    @Column(name = "event_organized_by",nullable = false)
    private String eventOrganizedBy;


    public Event(String title, String description, EventType eventType, String venue, LocalDate date, String eventOrganizedBy) {
        this.title = title;
        this.description = description;
        this.eventType = eventType;
        this.venue = venue;
        this.date = date;
        this.eventOrganizedBy = eventOrganizedBy;
    }

    @Override
    public String toString() {
        return "Event{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventType=" + eventType +
                ", venue='" + venue + '\'' +
                ", date=" + date +
                ", eventOrganizedBy='" + eventOrganizedBy + '\'' +
                '}';
    }
}

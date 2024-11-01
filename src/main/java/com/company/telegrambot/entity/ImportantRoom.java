package com.company.telegrambot.entity;

import com.company.telegrambot.enums.RoomType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "important_rooms")
@NoArgsConstructor
public class ImportantRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "photo_id", nullable = false)
    private String photoId;

    @Column(name = "responsibility", nullable = false)
    private String responsibility;


    @Column(name = "room_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoomType roomType;


    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;


    public ImportantRoom(String name, String location, String photoId, String responsibility, RoomType roomType, Integer floorNumber) {
        this.name = name;
        this.location = location;
        this.photoId = photoId;
        this.responsibility = responsibility;
        this.roomType = roomType;
        this.floorNumber = floorNumber;
    }
}

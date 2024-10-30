package com.company.telegrambot.dto.club;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateClubRequest {

    private String name;

    private String description;

    private String clubType;

    private String contact;
}

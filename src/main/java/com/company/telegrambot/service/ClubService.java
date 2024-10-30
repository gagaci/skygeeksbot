package com.company.telegrambot.service;

import com.company.telegrambot.dto.club.CreateClubRequest;
import com.company.telegrambot.entity.Club;
import com.company.telegrambot.enums.ClubType;
import org.springframework.data.domain.Page;

public interface ClubService {


    void addClub(Club request);

    Page<Club> findAll(int page,int pageSize);

    Page<Club> findAllByClubType(int page, int pageSize, ClubType clubType);
}

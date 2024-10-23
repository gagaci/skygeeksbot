package com.company.telegrambot.service;

import com.company.telegrambot.entity.Club;
import org.springframework.data.domain.Page;

public interface ClubService {


    void addClub(Club club);

    Page<Club> findAll(int page,int pageSize);
}

package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.Club;
import com.company.telegrambot.enums.ClubType;
import com.company.telegrambot.repository.ClubRepository;
import com.company.telegrambot.service.ClubService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;


    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;

    }


    @Override
    public void addClub(Club request) {
        clubRepository.save(request);
    }

    @Override
    public Page<Club> findAll(int page, int pageSize) {
        return clubRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<Club> findAllByClubType(int page, int pageSize, ClubType clubType) {
        return clubRepository.findAllByClubType(PageRequest.of(page, pageSize), clubType);
    }
}

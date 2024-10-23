package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.Club;
import com.company.telegrambot.repository.ClubRepository;
import com.company.telegrambot.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;

    @Override
    public void addClub(Club club) {
        clubRepository.save(club);
    }

    @Override
    public Page<Club> findAll(int page, int pageSize) {
        return clubRepository.findAll(PageRequest.of(page,pageSize));
    }
}

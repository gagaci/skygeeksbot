package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.CampusFacility;
import com.company.telegrambot.repository.CampusFacilityRepository;
import com.company.telegrambot.service.CampusFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampusFacilityServiceImpl implements CampusFacilityService {

    private final CampusFacilityRepository campusFacilityRepository;

    @Override
    public void addFacility(CampusFacility facility) {
        campusFacilityRepository.save(facility);
    }

    @Override
    public Page<CampusFacility> findAll(int page, int pageSize) {
        return campusFacilityRepository.findAll(PageRequest.of(page, pageSize));
    }
}

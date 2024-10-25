package com.company.telegrambot.service;

import com.company.telegrambot.entity.CampusFacility;
import org.springframework.data.domain.Page;

public interface CampusFacilityService {

    void addFacility(CampusFacility facility);

    Page<CampusFacility> findAll(int page, int pageSize);
}

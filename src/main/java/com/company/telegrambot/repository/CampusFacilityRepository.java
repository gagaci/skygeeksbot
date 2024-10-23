package com.company.telegrambot.repository;

import com.company.telegrambot.entity.CampusFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampusFacilityRepository extends JpaRepository<CampusFacility,Integer> {
}

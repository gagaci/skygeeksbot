package com.company.telegrambot.repository;

import com.company.telegrambot.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club,Integer> {

    @Override
    Page<Club> findAll(Pageable pageable);
}

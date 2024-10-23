package com.company.telegrambot.repository;

import com.company.telegrambot.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer>, JpaRepository<Event, Integer> {

    @Override
    Page<Event> findAll(Pageable pageable);
}

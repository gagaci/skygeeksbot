package com.company.telegrambot.repository;

import com.company.telegrambot.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Integer>, JpaRepository<Event, Integer> {

}

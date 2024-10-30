package com.company.telegrambot.service;

import com.company.telegrambot.entity.Event;
import com.company.telegrambot.enums.EventType;
import com.company.telegrambot.enums.State;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EventService {

    void addEvent(Event event);

    Page<Event> findAll(int page,int pageSize);
    Page<Event> findAllSocialEvents(int page, int pageSize, EventType eventType);
}

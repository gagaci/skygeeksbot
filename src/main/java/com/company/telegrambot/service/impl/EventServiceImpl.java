package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.Event;
import com.company.telegrambot.enums.EventType;
import com.company.telegrambot.repository.EventRepository;
import com.company.telegrambot.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;


    @Override
    public void addEvent(Event event) {
        eventRepository.save(event);
    }


    @Override
    public Page<Event> findAll(int page, int pageSize) {
        return eventRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<Event> findAllSocialEvents(int page, int pageSize, EventType eventType) {
        return eventRepository.findAllByEventType(PageRequest.of(page, pageSize), eventType);
    }
}

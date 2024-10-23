package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.Event;
import com.company.telegrambot.repository.EventRepository;
import com.company.telegrambot.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository repository;


    @Override
    public void addEvent(Event event) {
        repository.save(event);
    }

    @Override
    public List<Event> getEvents() {
       return repository.findAll();
    }
}

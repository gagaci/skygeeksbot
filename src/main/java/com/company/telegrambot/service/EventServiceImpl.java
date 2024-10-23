package com.company.telegrambot.service;

import com.company.telegrambot.entity.Event;
import com.company.telegrambot.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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

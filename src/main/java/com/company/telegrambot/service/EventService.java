package com.company.telegrambot.service;

import com.company.telegrambot.entity.Event;

import java.util.List;

public interface EventService {

    void addEvent(Event event);

    List<Event> getEvents();
}

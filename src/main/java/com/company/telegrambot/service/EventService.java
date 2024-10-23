package com.company.telegrambot.service;

import com.company.telegrambot.entity.Event;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EventService {

    void addEvent(Event event);



    Page<Event> findAll(int page,int pageSize);
}

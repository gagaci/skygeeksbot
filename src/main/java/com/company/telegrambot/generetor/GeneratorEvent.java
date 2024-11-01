package com.company.telegrambot.generetor;

import com.company.telegrambot.entity.Event;
import com.company.telegrambot.enums.EventType;
import com.company.telegrambot.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GeneratorEvent {

    private final EventService eventService;


    /// TEST event create
   public void createEvent() {
        var event = new Event("Session With MR John", " this is event for freshmen and seniors",
                "",
                EventType.SOCIAL,
                "North hole room 412",
                LocalDate.now().plusDays(5), "SGA");

        eventService.addEvent(event);
    }
}

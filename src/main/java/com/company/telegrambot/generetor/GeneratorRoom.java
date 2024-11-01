package com.company.telegrambot.generetor;

import com.company.telegrambot.entity.ImportantRoom;
import com.company.telegrambot.enums.RoomType;
import com.company.telegrambot.service.ImportantRoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeneratorRoom {

    private final ImportantRoomsService importantRoomsService;


    public void createImportantRoom() {
        var importantRoom = new ImportantRoom("Accounting",
                "109 North Hall",
                "AgACAgIAAxkDAAIH5Gckt6GIzMtVn62Fy91bZSwYJoxYAALq4TEb5VEgSTwb0mDQxtgOAQADAgADeQADNgQ",
                "Managing tuition payments, billing, and student financial records’", RoomType.ADMINISTRATIVE, 1);
        importantRoomsService.addRoom(importantRoom);
    }
}

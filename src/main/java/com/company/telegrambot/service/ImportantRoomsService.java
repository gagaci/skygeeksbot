package com.company.telegrambot.service;

import com.company.telegrambot.entity.ImportantRoom;
import org.springframework.data.domain.Page;

public interface ImportantRoomsService {

    void addRoom(ImportantRoom room);

    Page<ImportantRoom> findAll(int page,int pageSize);
}

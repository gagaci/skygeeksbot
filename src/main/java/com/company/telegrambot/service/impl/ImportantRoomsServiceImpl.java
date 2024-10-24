package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.ImportantRoom;
import com.company.telegrambot.repository.ImportantRoomRepository;
import com.company.telegrambot.service.ImportantRoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImportantRoomsServiceImpl implements ImportantRoomsService {

    private final ImportantRoomRepository importantRoomRepository;

    @Override
    public void addRoom(ImportantRoom room) {
        importantRoomRepository.save(room);
    }

    @Override
    public Page<ImportantRoom> findAll(int page, int pageSize) {
        return importantRoomRepository.findAll(PageRequest.of(page, pageSize));
    }
}

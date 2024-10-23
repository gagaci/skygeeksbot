package com.company.telegrambot.repository;

import com.company.telegrambot.entity.ImportantRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportantRoomRepository extends JpaRepository<ImportantRoom,Integer> {
}

package com.company.telegrambot.loader;

import com.company.telegrambot.entity.Club;
import com.company.telegrambot.repository.ClubRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseLoader {

    public CommandLineRunner initDatabase(ClubRepository clubRepository){
        return args -> {
          clubRepository.save(new Club("Fan of Lord of Rings","xaxa","6767612"));
        };
    }
}

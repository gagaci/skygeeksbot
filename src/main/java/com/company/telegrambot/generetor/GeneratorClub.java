package com.company.telegrambot.generetor;

import com.company.telegrambot.entity.Club;
import com.company.telegrambot.enums.ClubType;
import com.company.telegrambot.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GeneratorClub {

    private final ClubService clubService;

   public void createSpanishClub() {
        var club = new Club("Spanish Club",
                ClubType.LANGUAGE,
                "Bienvenido's amigos! \uD83C\uDDEA\uD83C\uDDF8 Do you want to speak and communicate with Spanish speakers? Come to our classes",
                "Ng",
                "@username",
                "https://t.me/+FDo5gxtDQQowOWZi");
        clubService.addClub(club);
    }

  public   void createDotaClub() {
        var club = new Club("Dota Club",
                ClubType.GAME,
                "Bienvenido's amigos! \uD83C\uDDEA\uD83C\uDDF8 Do you want to speak and communicate with Spanish speakers? Come to our classes",
                "Abdullu",
                "@username",
                "Dota");
        clubService.addClub(club);
    }
  public   void createCSClub() {
        var club = new Club("CS Club",
                ClubType.GAME,
                "Bienvenido's amigos! \uD83C\uDDEA\uD83C\uDDF8 Do you want to speak and communicate with Spanish speakers? Come to our classes",
                "Abdullu",
                "@username",
                "cs");
        clubService.addClub(club);
    }

   public void createArabicClub() {
        var club = new Club("Arabic Club",
                ClubType.LANGUAGE,
                "! \uD83C\uDF19 Want to dive into the beauty of the Arabic language? Join our fun and interactive sessions with Rubina",
                "Mubina",
                "@username",
                "https://t.me/arabicclubwut");
        clubService.addClub(club);
    }

   public void createGermanClub() {
        var club = new Club("German Club",
                ClubType.LANGUAGE,
                "Hallo! \uD83C\uDF0D Join Bekzod in learning German and explore one of the most widely spoken languages in Europe",
                "Beka",
                "@username",
                "https://t.me/DeutschWebster");
        clubService.addClub(club);
    }
}

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
                "AgACAgIAAxkDAAIHj2ckqHpJgC5MOUM3nHT7LXN4ZMSEAAJM4DEb5VEgScVj3WxUkc9QAQADAgADeQADNgQ",
                "Bienvenido's amigos! \uD83C\uDDEA\uD83C\uDDF8 Do you want to speak and communicate with Spanish speakers? Come to our classes",
                "Nigina",
                "@username",
                "https://t.me/+FDo5gxtDQQowOWZi");
        clubService.addClub(club);
    }

  public   void createCyberSportClub() {
        var club = new Club("Cyber sport club",
                ClubType.GAME,
                "AgACAgIAAxkDAAIHjmckqDDpCM1DU_KD1wABzQxi7LsIrAACSeAxG-VRIEkHJ5uH9dR7OQEAAwIAA3kAAzYE",
                "Calling all gamers! \uD83C\uDFAE Join Anvar’s Cyber Sport club and compete in the world of e-sports!",
                "Abdullu",
                "@username",
                "https://t.me/webstercybersport");
        clubService.addClub(club);
    }


   public void createArabicClub() {
        var club = new Club("Arabic Club",
                ClubType.LANGUAGE,
                "AgACAgIAAxkDAAIHkGckqKERHfynZllU8fWy6zYZPReTAAJO4DEb5VEgSaevKDWR5VYrAQADAgADeQADNgQ",
                "! \uD83C\uDF19 Want to dive into the beauty of the Arabic language? Join our fun and interactive sessions with Rubina",
                "Mubina",
                "@username",
                "https://t.me/arabicclubwut");
        clubService.addClub(club);
    }

    public void createSelfDefenceClub() {
        var club = new Club("Self Defense Club",
                ClubType.SPORT,
                "AgACAgIAAxkDAAIHrWckqxaeTEzKDc9B1FqBQkznrUQyAAKD4DEb5VEgSZvNpw690IYkAQADAgADeQADNgQ",
                "Empower yourself with self-defense skills! \uD83E\uDD4B Join Alan’s sessions and feel confident and strong",
                "John",
                "@username",
                "https://t.me/+RO1BrZk8HqU2YWEy");
        clubService.addClub(club);
    }
    public void createFilmMakingClub() {
        var club = new Club("Filmmaking and Photography Club",
                ClubType.OTHER,
                "AgACAgIAAxkDAAIHrmckq1UoIKpE417t4Vw3pIzVb14nAAKP4DEb5VEgSfEU1vfVHbY2AQADAgADeAADNgQ",
                "Lights, camera, action! \uD83C\uDFA5 Join Malika in capturing stories through filmmaking and photography at Webster.g",
                "Abdulaziz",
                "@username",
                "https://t.me/Webster_filmmaking");
        clubService.addClub(club);
    }



}

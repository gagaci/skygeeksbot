//package com.company.telegrambot.controller;
//
//import com.company.telegrambot.common.Path;
//import com.company.telegrambot.dto.club.CreateClubRequest;
//import com.company.telegrambot.service.ClubService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(Path.CLUB)
//@RequiredArgsConstructor
//@Tag(name = "Club")
//public class ClubController {
//
//    private final ClubService clubService;
//
//
//    @PostMapping
//    public void createClub(@RequestBody CreateClubRequest request){
//        clubService.addClub(request);
//    }
//}

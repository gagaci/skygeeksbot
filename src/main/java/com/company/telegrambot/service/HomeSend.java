package com.company.telegrambot.service;

import com.company.telegrambot.enums.State;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HomeSend {

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramBot telegramBot;

    public void startCommandReceived(long chatId, String username) {
        String answer = EmojiParser.parseToUnicode("Hi, " + username + ", nice to meet you!" + " :blush:");
        log.info("Replied to user {} ", username);

        telegramBot.sendMessage(chatId, answer);

        userService.setState(chatId, State.HOME);
    }



}

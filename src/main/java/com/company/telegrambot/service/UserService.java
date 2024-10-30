package com.company.telegrambot.service;

import com.company.telegrambot.entity.User;
import com.company.telegrambot.enums.State;

public interface UserService {

    State getUserState(Long id);

    void setState(Long chatId,State state);

    User getOne(Long id);


}

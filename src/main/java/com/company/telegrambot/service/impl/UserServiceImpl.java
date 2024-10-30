package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.User;
import com.company.telegrambot.enums.State;
import com.company.telegrambot.repository.UserRepository;
import com.company.telegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public State getUserState(Long id) {
        User user = userRepository.findById(id).get();
        return Objects.requireNonNull(user).getState();
    }

    @Override
    public void setState(Long chatId, State state) {
        User user = getOne(chatId);
        user.setState(state);
        userRepository.save(user);
    }

    @Override
    public User getOne(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}

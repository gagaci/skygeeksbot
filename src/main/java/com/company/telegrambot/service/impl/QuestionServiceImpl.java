package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.Question;
import com.company.telegrambot.entity.User;
import com.company.telegrambot.repository.QuestionRepository;
import com.company.telegrambot.service.QuestionService;
import com.company.telegrambot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final UserService userService;

    private final QuestionRepository questionRepository;


    @Override
    public void addQuestion(String question, Long userId) {
        User user = userService.getOne(userId);
        var entity = new Question(question, user);
        questionRepository.save(entity);
    }
}

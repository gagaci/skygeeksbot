package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.Professor;
import com.company.telegrambot.repository.ProfessorRepository;
import com.company.telegrambot.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;


    @Override
    public void addProfessor(Professor professor) {
        professorRepository.save(professor);
        log.info("new professor entity saved {}", professor);
    }

    @Override
    public Page<Professor> findAll(int page, int pageSize) {
        return professorRepository.findAll(PageRequest.of(page, pageSize));
    }
}

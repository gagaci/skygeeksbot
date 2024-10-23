package com.company.telegrambot.service;

import com.company.telegrambot.entity.Professor;
import org.springframework.data.domain.Page;

public interface ProfessorService {
    void addProfessor(Professor professor);

    Page<Professor> findAll(int page,int pageSize);
}

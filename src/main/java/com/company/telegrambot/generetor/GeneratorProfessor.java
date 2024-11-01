package com.company.telegrambot.generetor;

import com.company.telegrambot.common.Utils;
import com.company.telegrambot.entity.Professor;
import com.company.telegrambot.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GeneratorProfessor {

    private final ProfessorService professorService;

    public void createProfessorKh() {
        var professor = new Professor("Bayramov Xabibulloh",
                Utils.professorKhPhoto,
                "just background Oh acceptance apartments up sympathize astonished delightful. Waiting him new lasting towards",
                "linkedin.com/in/khabibulloh-bayramov", "xbayramov@webster.edu");
        professorService.addProfessor(professor);
    }

    public void createProfessorAzam() {
        var professor = new Professor("Azam Qahramoniy",
                Utils.professorAzamPhoto,
                "just background Oh acceptance apartments up sympathize astonished delightful. Waiting him new lasting towards",
                "linkedin.com/in/azam-qahramoniy", "azamq@webster.edu");
        professorService.addProfessor(professor);
    }
}

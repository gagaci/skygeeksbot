package com.company.telegrambot.service;

import com.company.telegrambot.entity.FAQ;
import org.springframework.data.domain.Page;

public interface FAQService {

    void addFAQ(FAQ faq);

    Page<FAQ> findAll(int page, int pageSize);
}

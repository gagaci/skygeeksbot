package com.company.telegrambot.service.impl;

import com.company.telegrambot.entity.FAQ;
import com.company.telegrambot.repository.FAQRepository;
import com.company.telegrambot.service.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

    private final FAQRepository faqRepository;

    @Override
    public void addFAQ(FAQ faq) {
        faqRepository.save(faq);
    }

    @Override
    public Page<FAQ> findAll(int page, int pageSize) {
        return faqRepository.findAll(PageRequest.of(page, pageSize));
    }
}

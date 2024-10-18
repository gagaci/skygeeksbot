package com.company.telegrambot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    @Value(value = "${bot.name}")
   private String botName;

    @Value(value = "${bot.key}")
    private String token;
}

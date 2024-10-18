package com.company.telegrambot.service;

import com.company.telegrambot.BotState;
import com.company.telegrambot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private static final String HELP_TEXT = "This bot is for Webster Tashkent students to be familiar with the university.\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:\n\n" +
            "Type /start to start the bot\n\n" +
            "Type /events to see upcoming events\n\n" +
            "Type /help to see this message again";
    private Map<Long, BotState> botSate = new HashMap<>();

    public BotState getState(Long userId) {
        return botSate.getOrDefault(userId, BotState.START);
    }

    public void setState(Long userId, BotState state) {
        botSate.put(userId, state);
    }

    final BotConfig config;

    public TelegramBot(BotConfig config, TomcatServletWebServerFactory tomcatServletWebServerFactory) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("start", "get a welcome message"));
        listofCommands.add(new BotCommand("event", "get info about upcoming events"));
        listofCommands.add(new BotCommand("important_rooms", "delete my data"));
        listofCommands.add(new BotCommand("help", "info how to use this bot"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }


    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public String getBaseUrl() {
        return super.getBaseUrl();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long userId = update.getMessage().getChatId();
        BotState currentState = getState(userId);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    sendMenu(update.getMessage().getChatId());
                    setState(update.getMessage().getChatId(), BotState.MENU);
                    break;
                case "/events":
                    eventCommandReceived(chatId);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        }
    }

    private void startCommandReceived(long chatId, String username) {
        String answer = "Hi, " + username + ", nice to meet you!";
        log.info("Replied to user {} ", username);
        sendMessage(chatId, answer);
    }

    private void eventCommandReceived(long chatId) {
        String event = "Upcoming event is fun";
        sendMessage(chatId, event);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Welcome to the menu! Please choose an option:");

        // Create ReplyKeyboardMarkup
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Make the keyboard fit the screen size
        keyboardMarkup.setOneTimeKeyboard(false); // Keeps the keyboard visible

        // Create KeyboardRow objects
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Events 1"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Important rooms 2"));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Option 3"));

        // Add rows to the keyboard
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);

        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboardRows);

        // Attach the keyboard to the message
        message.setReplyMarkup(keyboardMarkup);

        // Send the message using execute() method
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}

package com.company.telegrambot.service;

import com.company.telegrambot.BotState;
import com.company.telegrambot.config.BotConfig;
import com.company.telegrambot.entity.Event;
import com.company.telegrambot.entity.User;
import com.company.telegrambot.enums.EventType;
import com.company.telegrambot.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.SystemColor.text;

@Service
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private static final String ERROR_TEXT = "Error";
    private static final String YES_BUTTON = "YES_BUTTON";
    private static final String EVENT_BUTTON = "EVENT_BUTTON";
    private static final String NO_BUTTON = "NO_BUTTON";

    @Autowired
    private EventService eventService;


    @Autowired
    private UserRepository userRepository;


    private static final String HELP_TEXT = "This bot is for Webster Tashkent students to be familiar with the university.\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:\n\n" +
            "Type /start to start the bot\n\n" +
            "Type /events to see upcoming events\n\n" +
            "Type /help to see this message again";


    final BotConfig config;

    public TelegramBot(BotConfig config, TomcatServletWebServerFactory tomcatServletWebServerFactory) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("start", "get a welcome message"));
        listofCommands.add(new BotCommand("events \uD83C\uDFB8", "get info about upcoming events vinland saga"));
        listofCommands.add(new BotCommand("events", "get info about upcoming events vinland saga"));
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    registerUser(update.getMessage());
                    break;
                case "/register":
                    register(chatId);
                    break;
                case "/events":
                    eventCommandReceived(chatId);
                    break;
                case "events 🎸":  // Adjust this to match your button label
                    eventCommandReceived(chatId);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(YES_BUTTON)) {
                String text = "yes";
                executeEditMessageText(text, chatId, messageId);
            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "You pressed NO button";
                executeEditMessageText(text, chatId, messageId);
            }

        }
    }

    private void executeEditMessageText(String text, long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId(messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT, e.getMessage());
        }
    }

    private void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Events");
        yesButton.setCallbackData(EVENT_BUTTON);

        var noButton = new InlineKeyboardButton();
        noButton.setText("No");
        noButton.setCallbackData(NO_BUTTON);
        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);

        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
        }
    }

    private void startCommandReceived(long chatId, String username) {
        String answer = EmojiParser.parseToUnicode("Hi, " + username + ", nice to meet you!" + " :blush:");
        log.info("Replied to user {} ", username);
        sendMessage(chatId, answer);
    }

    private void eventCommandReceived(long chatId) {
//        createEvent();
        var events = eventService.getEvents();
        String hm = "[eventType] = Social Event \n" +
                "[title] =  Session with Mr John \n" +
                "[description] = this is event for freshmen and seniors\n" +
                "[venue] = North hole room 412\n" +
                "[date] = 2024-05-10\n" +
                "[organizedBy] = SGA";

        String temple = "This is %s event\n" +
                "%s \n" +
                "Description : %s\n" +
                "Venue: %s\n" +
                "Date : %s\n" +
                "Organized by : %s";
        events.forEach(event -> {

            sendMessage(chatId, String.format(temple, event.getEventType().toString(),
                    event.getTitle(), event.getDescription(), event.getVenue(), event.getDate().toString(), event.getEventOrganizedBy()));
        });
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        displayMenu(message,chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayMenu(SendMessage message,long chatId) {

        String messageText = message.getText();

        String guitar = "\uD83C\uDFB8";

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("events \uD83C\uDFB8");
        row.add("important rooms ❕");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("professors  \uD83D\uDC69\uD83C\uDFFC\u200D\uD83C\uDFEB");
        row.add("clubs  \uD83C\uDFF9");
        row.add("university facilities  \uD83E\uDDD8\uD83C\uDFFB\u200D♂\uFE0F");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);


    }


    private void registerUser(Message msg) {

        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            var user = new User(chatId, chat.getFirstName(), LocalDateTime.now());
            userRepository.save(user);
            log.info("user saved {} ", user);
        }
    }

    void createEvent() {
        var event = new Event("Session With MR John", " this is event for freshmen and seniors", EventType.SOCIAL, "North hole room 412", LocalDate.now().plusDays(5), "SGA");
        eventService.addEvent(event);
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}

package com.company.telegrambot.service;

import com.company.telegrambot.common.Utils;
import com.company.telegrambot.config.BotConfig;
import com.company.telegrambot.entity.*;
import com.company.telegrambot.enums.ClubType;
import com.company.telegrambot.enums.EventType;
import com.company.telegrambot.enums.RoomType;
import com.company.telegrambot.enums.State;
import com.company.telegrambot.generetor.GeneratorClub;
import com.company.telegrambot.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
import java.util.List;

@Service
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private static final String NEXT_BUTTON = "NEXT_BUTTON";
    private static final String PREV_BUTTON = "PREV_BUTTON";

    @Autowired
    private CampusFacilityService campusFacilityService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ImportantRoomsService importantRoomsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneratorClub generatorClub;

    private int currentPage = 0;


    private static final String HELP_TEXT = """
            Hello, dear student!
            
            📣 If you’re a freshman and don’t understand the ins and outs of Webster, this bot is for you!
            
            With the Webster Onboarding Bot, you can learn about:
            🎓 Using Canvas
            🏇 Clubs
            🎸 Events
            👩‍🏫 Professors
            🏢 Facilities""";


    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("start", "get a welcome message"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: {}", e.getMessage());
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
            registerUser(update.getMessage());
            State state = userService.getUserState(chatId);

            switch (messageText.toLowerCase()) {
                case "/start":
                    sendHomeMessage(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    switch (state) {
                        case HOME:
                            homeState(messageText, chatId, update.getMessage().getChat().getFirstName());
                            break;
                        case EVENTS:
                            eventState(messageText, chatId);
                            break;
                        case CLUBS:
                            clubState(messageText, chatId);
                            break;
                        case PROFESSORS:
                            professorState(messageText, chatId);
                            break;
                        case FACILITIES:
                            facilityState(messageText, chatId);
                            break;
                        case ROOMS:
                            roomState(messageText, chatId);
                            break;
                    }
            }


        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String determiner = update.getCallbackQuery().getMessage().getText();
            if (callbackData.equals(NEXT_BUTTON)) {
                currentPage++;
                if (determiner.startsWith("Social events:")) {
                    socialEventCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Academic events:")) {
                    academicEventCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Orientation events:")) {
                    orientationEventCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Language clubs:")) {
                    languageClubReceived(chatId, currentPage);
                } else if (determiner.startsWith("Game clubs:")) {
                    gameClubReceived(chatId, currentPage);
                } else if (determiner.startsWith("Sport clubs:")) {
                    sportClubReceived(chatId, currentPage);
                } else if (determiner.startsWith("Other clubs:")) {
                    otherClubReceived(chatId, currentPage);
                } else if (determiner.startsWith("Professors:")) {
                    allProfessorsReceived(chatId, currentPage);
                } else if (determiner.startsWith("Important rooms:")) {
                    allRoomsReceived(chatId, currentPage);
                } else if (determiner.startsWith("Campus facilities:")) {
                    allFacilitiesReceived(chatId, currentPage);
                }
            } else if (callbackData.equals(PREV_BUTTON)) {
                if (currentPage > 0) {
                    currentPage--;
                }
                if (determiner.startsWith("Social events:")) {
                    socialEventCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Academic events:")) {
                    academicEventCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Orientation events:")) {
                    orientationEventCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Language clubs:")) {
                    languageClubReceived(chatId, currentPage);
                } else if (determiner.startsWith("Game clubs:")) {
                    gameClubReceived(chatId, currentPage);
                } else if (determiner.startsWith("Sport clubs:")) {
                    sportClubReceived(chatId, currentPage);
                } else if (determiner.startsWith("Other clubs:")) {
                    otherClubReceived(chatId, currentPage);
                } else if (determiner.startsWith("Professors:")) {
                    allProfessorsReceived(chatId, currentPage);
                } else if (determiner.startsWith("Campus facilities:")) {
                    allFacilitiesReceived(chatId, currentPage);
                } else if (determiner.startsWith("Important rooms:")) {
                    allRoomsReceived(chatId, currentPage);
                }
            }


        }
    }


    private void homeState(String messageText, long chatId, String firstname) {
        switch (messageText) {
            case "/start":
                sendHomeMessage(chatId, firstname);
                break;
            case "events 🎸":
                eventCommandReceived(chatId);
                break;
            case "professors 👩‍🏫":
                professorCommandReceived(chatId);
                break;
            case "important rooms ❕":
                importantRoomsCommandReceived(chatId);
                break;
            case "clubs 🏇":
                clubCommandReceived(chatId);
                break;
            case "university facilities 🧘‍♂️":
                campusFacilityCommandReceived(chatId);
                break;
            case "/help":
                sendMessage(chatId, HELP_TEXT);
                break;
            default:
                sendMessage(chatId, "Sorry, command was not recognized");
        }
    }


    private void eventState(String messageText, long chatId) {
        switch (messageText) {
            case "social":
                socialEventCommandReceived(chatId, currentPage);
                break;
            case "academic":
                academicEventCommandReceived(chatId, currentPage);
                break;
            case "orientation":
                orientationEventCommandReceived(chatId, currentPage);
                break;
            case "back ↩️":
                sendHomeMessage(chatId, "");
                break;
            default:
                sendMessage(chatId, "Sorry, command was not recognized");
        }
    }

    private void clubState(String messageText, long chatId) {
        switch (messageText) {
            case "language":
                languageClubReceived(chatId, currentPage);
                break;
            case "sport":
                sportClubReceived(chatId, currentPage);
                break;
            case "game":
                gameClubReceived(chatId, currentPage);
                break;
            case "other":
                otherClubReceived(chatId, currentPage);
                break;
            case "back ↩️":
                sendHomeMessage(chatId, "");
                break;
            default:
                sendMessage(chatId, "Sorry, command was not recognized");
        }
    }

    private void professorState(String messageText, long chatId) {
        switch (messageText) {
            case "all":
                allProfessorsReceived(chatId, currentPage);
                break;
            case "back ↩️":
                sendHomeMessage(chatId, "");
                break;
            default:
                sendMessage(chatId, "Sorry, command was not recognized");
        }
    }

    private void facilityState(String messageText, long chatId) {
        switch (messageText) {
            case "all":
                allFacilitiesReceived(chatId, currentPage);
                break;
            case "back ↩️":
                sendHomeMessage(chatId, "");
                break;
            default:
                sendMessage(chatId, "Sorry, command was not recognized");
        }
    }

    private void roomState(String messageText, long chatId) {
        switch (messageText) {
            case "all":
                allRoomsReceived(chatId, currentPage);
                break;
            case "back ↩️":
                sendHomeMessage(chatId, "");
                break;
            default:
                sendMessage(chatId, "Sorry, command was not recognized");
        }
    }


    public void sendHomeMessage(long chatId, String username) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        String answer = EmojiParser.parseToUnicode("Hi, welcome to Webster university's onboarding bot" + " ✈️");
        log.info("Replied to user {} ", username);
        message.setText(answer);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add(Utils.EVENTS);
        row.add("important rooms ❕");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("professors 👩‍🏫");
        row.add(Utils.CLUBS);
        row.add("university facilities 🧘‍♂️");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
            userService.setState(chatId, State.HOME);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendClubMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select category:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("language");
        row.add("sport");
        row.add("game");
        row.add("others");
        row.add("back ↩️");

        keyboardRows.add(row);


        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEventMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select category:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("social");
        row.add("academic");
        row.add("orientation");
        row.add("back ↩️");

        keyboardRows.add(row);


        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendProfessorMessage(long chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select option:");


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("all");
        row.add("back ↩️");

        keyboardRows.add(row);


        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendFacilityMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select option:");


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("all");
        row.add("back ↩️");

        keyboardRows.add(row);


        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendRoomMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Select option:");


        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("all");
        row.add("back ↩️");

        keyboardRows.add(row);


        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerUser(Message msg) {
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            var user = new User(chatId, chat.getFirstName(), State.HOME, LocalDateTime.now());
            userRepository.save(user);
            log.info("user saved {} ", user);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }


    /// EVENTS
    private void eventCommandReceived(long chatId) {

        sendEventMessage(chatId);
        userService.setState(chatId, State.EVENTS);

    }

    private void socialEventCommandReceived(long chatId, int page) {
        var events = eventService.findAllSocialEvents(page, 1, EventType.SOCIAL);

        StringBuilder messageText = new StringBuilder("Social events:\n");

        for (Event event : events) {
            String temple = """
                    This is %s event
                    %s\s
                    Description : %s
                    Venue: %s
                    Date : %s
                    Organized by : %s""";

            String message = String.format(temple, event.getEventType().toString().toLowerCase(),
                    event.getTitle(), event.getDescription(), event.getVenue(), event.getDate().toString(), event.getEventOrganizedBy());
            messageText.append(message).append("\n");
            log.info("Response Social Events {}", events);
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markUpInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        if (events.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInLine.add(prevButton);
        }
        if (events.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInLine.add(nextButton);
        }
        rowsInLine.add(rowInLine);
        markUpInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markUpInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending social events list: {}", e.getMessage());
        }
    }

    private void academicEventCommandReceived(long chatId, int page) {
        var events = eventService.findAllSocialEvents(page, 1, EventType.ACADEMIC);

        StringBuilder messageText = new StringBuilder("Academic events:\n");

        for (Event event : events) {
            String temple = """
                    This is %s event
                    %s\s
                    Description : %s
                    Venue: %s
                    Date : %s
                    Organized by : %s""";

            String message = String.format(temple, event.getEventType().toString().toLowerCase(),
                    event.getTitle(), event.getDescription(), event.getVenue(), event.getDate().toString(), event.getEventOrganizedBy());
            messageText.append(message).append("\n");
            log.info("Response Academic Events {}", events);
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markUpInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        if (events.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInLine.add(prevButton);
        }
        if (events.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInLine.add(nextButton);
        }
        rowsInLine.add(rowInLine);
        markUpInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markUpInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending Events list: {}", e.getMessage());
        }
    }

    private void orientationEventCommandReceived(long chatId, int page) {
        var events = eventService.findAllSocialEvents(page, 1, EventType.ORIENTATION);

        StringBuilder messageText = new StringBuilder("Orientation events:\n");

        for (Event event : events) {
            String temple = """
                    This is %s event
                    %s\s
                    Description : %s
                    Venue: %s
                    Date : %s
                    Organized by : %s""";

            String message = String.format(temple, event.getEventType().toString().toLowerCase(),
                    event.getTitle(), event.getDescription(), event.getVenue(), event.getDate().toString(), event.getEventOrganizedBy());
            messageText.append(message).append("\n");
            log.info("Response Orientation Events {}", events);
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markUpInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        if (events.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInLine.add(prevButton);
        }
        if (events.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInLine.add(nextButton);
        }
        rowsInLine.add(rowInLine);
        markUpInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markUpInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending Orientation Events list: {}", e.getMessage());
        }
    }

    /// CLUBS
    private void clubCommandReceived(long chatId) {

        sendClubMessage(chatId);
        userService.setState(chatId, State.CLUBS);

    }

    private void languageClubReceived(long chatId, int page) {
        generatorClub.createArabicClub();
        generatorClub.createSpanishClub();
        var clubsPage = clubService.findAllByClubType(page, 1, ClubType.LANGUAGE);
        StringBuilder messageText = new StringBuilder("Language clubs:\n");
        for (Club club : clubsPage) {
            String temple = """
                    Club name : %s
                    Description : %s
                    Founder : %s
                    Founder contact: %s
                    Contact: %s""";
            String message = String.format(temple, club.getName(), club.getDescription(), club.getFounder(), club.getFounderContact(), club.getContact());
            messageText.append(message).append("\n");
            log.info("Response language Clubs {}", club);

        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (clubsPage.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInline.add(prevButton);
        }

        if (clubsPage.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInline.add(nextButton);
        }

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending Language clubs list: {}", e.getMessage());
        }
    }

    private void sportClubReceived(long chatId, int page) {
        var clubsPage = clubService.findAllByClubType(page, 1, ClubType.SPORT);
        StringBuilder messageText = new StringBuilder("Sport clubs:\n");
        for (Club club : clubsPage) {
            String temple = """
                    Club name : %s
                    Description : %s
                    Founder : %s
                    Founder contact : %s
                    Contact: %s""";
            String message = String.format(temple, club.getName(), club.getDescription(), club.getFounder(), club.getFounderContact(), club.getContact());
            messageText.append(message).append("\n");
            log.info("Response sport Clubs {}", club);

        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (clubsPage.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInline.add(prevButton);
        }

        if (clubsPage.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInline.add(nextButton);
        }

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending Campus Facility list: {}", e.getMessage());
        }
    }

    private void gameClubReceived(long chatId, int page) {
        generatorClub.createDotaClub();
        generatorClub.createCSClub();
        var clubsPage = clubService.findAllByClubType(page, 1, ClubType.GAME);
        StringBuilder messageText = new StringBuilder("Game clubs:\n");
        for (Club club : clubsPage) {
            String temple = """
                    Club name : %s
                    Description : %s
                    Founder : %s
                    Founder contact : %s
                    Contact: %s""";
            String message = String.format(temple, club.getName(), club.getDescription(), club.getFounder(), club.getFounderContact(), club.getContact());
            messageText.append(message).append("\n");
            log.info("Response game Clubs {}", club);

        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (clubsPage.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInline.add(prevButton);
        }

        if (clubsPage.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInline.add(nextButton);
        }

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error game clubs list: {}", e.getMessage());
        }
    }

    private void otherClubReceived(long chatId, int page) {
        var clubsPage = clubService.findAllByClubType(page, 1, ClubType.OTHER);
        StringBuilder messageText = new StringBuilder("Other clubs:\n");
        for (Club club : clubsPage) {
            String temple = """
                    Club name : %s
                    Description : %s
                    Founder : %s
                    Founder contact : %s
                    Contact: %s""";
            String message = String.format(temple, club.getName(), club.getDescription(), club.getFounder(), club.getFounderContact(), club.getContact());
            messageText.append(message).append("\n");
            log.info("Response other Clubs {}", club);

        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (clubsPage.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInline.add(prevButton);
        }

        if (clubsPage.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInline.add(nextButton);
        }

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error other clubs list: {}", e.getMessage());
        }
    }

    private void allProfessorsReceived(long chatId, int page) {

        createProfessor();
        createProfessor();
        createProfessor();
        createProfessor();

        var professors = professorService.findAll(page, 1);

        StringBuilder messageText = new StringBuilder("Professors:\n");

        for (Professor professor : professors) {
            String temple = """
                    Full name: %s\s
                    Background: %s
                    linkedIn account: %s
                    email: %s""";

            String message = String.format(temple, professor.getFullName(),
                    professor.getBackground(), professor.getLinkedInAccount(), professor.getEmail());
            messageText.append(message).append("\n");
            log.info("Response Professors {}", professors);
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markUpInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        if (professors.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInLine.add(prevButton);
        }
        if (professors.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInLine.add(nextButton);
        }
        rowsInLine.add(rowInLine);
        markUpInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markUpInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending Professors list: {}", e.getMessage());
        }
    }

    private void professorCommandReceived(long chatId) {

        sendProfessorMessage(chatId);
        userService.setState(chatId, State.PROFESSORS);


    }

    private void allFacilitiesReceived(long chatId, int page) {
        createGymFacility();
        createTherapistFacility();
        createFoodCourtFacility();
        var facilityPage = campusFacilityService.findAll(page, 1);
        StringBuilder messageText = new StringBuilder("Campus facilities:\n");
        for (CampusFacility facility : facilityPage) {
            String temple = """
                    Facility : %s
                    Location : %s
                    Contact: %s""";
            String message = String.format(temple, facility.getFacility(), facility.getLocation(), facility.getContact());
            messageText.append(message).append("\n");
            log.info("Response Facilities {}", facilityPage);

        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (facilityPage.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInline.add(prevButton);
        }

        if (facilityPage.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInline.add(nextButton);
        }

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending Facilities list: {}", e.getMessage());
        }
    }

    private void allRoomsReceived(long chatId, int page) {
        createImportantRoom();
        createImportantRoom();
        createImportantRoom();
        var importantRooms = importantRoomsService.findAll(page, 1);

        StringBuilder messageText = new StringBuilder("Important rooms:\n");

        for (ImportantRoom importantRoom : importantRooms) {
            String temple = """
                    The room location: %s
                    Responsibility of the room: %s\s
                    Room type: %s
                    Floor: %s""";

            String message = String.format(temple, importantRoom.getLocation(), importantRoom.getResponsibility(), importantRoom.getRoomType(), importantRoom.getFloorNumber());
            messageText.append(message).append("\n");
            log.info("Response Important rooms {}", importantRooms.getTotalElements());
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText.toString());

        InlineKeyboardMarkup markUpInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        if (importantRooms.hasPrevious()) {
            var prevButton = new InlineKeyboardButton();
            prevButton.setText("Previous");
            prevButton.setCallbackData(PREV_BUTTON);
            rowInLine.add(prevButton);
        }
        if (importantRooms.hasNext()) {
            var nextButton = new InlineKeyboardButton();
            nextButton.setText("Next");
            nextButton.setCallbackData(NEXT_BUTTON);
            rowInLine.add(nextButton);
        }
        rowsInLine.add(rowInLine);
        markUpInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markUpInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending Important rooms list: {}", e.getMessage());
        }
    }
    private void importantRoomsCommandReceived(long chatId) {
        sendRoomMessage(chatId);
        userService.setState(chatId,State.ROOMS);
    }


    private void campusFacilityCommandReceived(long chatId) {
        sendFacilityMessage(chatId);
        userService.setState(chatId, State.FACILITIES);
    }


    /// TEST create campus facilities

    private void createGymFacility() {
        var facility = new CampusFacility("Gym facility", "North Hall floor 2, room 5", "@username");
        campusFacilityService.addFacility(facility);
    }

    private void createTherapistFacility() {
        var facility = new CampusFacility("Therapist facility", "North Hall floor 2, room 5", "@username");
        campusFacilityService.addFacility(facility);
    }

    private void createFoodCourtFacility() {
        var facility = new CampusFacility("Food Court", "North Hall floor 2, room 5", "@username");
        campusFacilityService.addFacility(facility);
    }

    /// TEST create an important
    private void createImportantRoom() {
        var importantRoom = new ImportantRoom("on the left-hand of hall", "Avising room", RoomType.ADMINISTRATIVE, 2);
        importantRoomsService.addRoom(importantRoom);
    }

    /// TEST professor create
    private void createProfessor() {
        var professor = new Professor("Bayramov Xabibulloh",
                "just background Oh acceptance apartments up sympathize astonished delightful. Waiting him new lasting towards",
                "linkedin.com/in/khabibulloh-bayramov", "xbayramov@webster.edu");
        professorService.addProfessor(professor);
    }

    /// TEST event create
    void createEvent() {
        var event = new Event("Session With MR John", " this is event for freshmen and seniors", EventType.SOCIAL, "North hole room 412", LocalDate.now().plusDays(5), "SGA");
        eventService.addEvent(event);
    }


}

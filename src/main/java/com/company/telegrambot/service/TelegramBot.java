package com.company.telegrambot.service;

import com.company.telegrambot.config.BotConfig;
import com.company.telegrambot.entity.*;
import com.company.telegrambot.enums.EventType;
import com.company.telegrambot.enums.RoomType;
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
    private EventService eventService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ImportantRoomsService importantRoomsService;

    @Autowired
    private UserRepository userRepository;

    private int currentPage = 0;


    private static final String HELP_TEXT = """
            This bot is for Webster Tashkent students to be familiar with the university.
            
            You can execute commands from the main menu on the left or by typing a command:
            
            Type /start to start the bot
            
            Type /events to see upcoming events
            
            Type /help to see this message again""";


    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("start", "get a welcome message"));
        listOfCommands.add(new BotCommand("clubs 🏇", "get info about clubs of the university"));
        listOfCommands.add(new BotCommand("events", "get info about upcoming events"));
        listOfCommands.add(new BotCommand("professors 👩‍🏫", "get info about professor in the uni"));
        listOfCommands.add(new BotCommand("university facilities 🧘‍♂️", "get info about campus facilities   "));
        listOfCommands.add(new BotCommand("important rooms ❕", "get info about important rooms in the uni"));
        listOfCommands.add(new BotCommand("help", "info how to use this bot"));
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
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    registerUser(update.getMessage());
                    break;
                case "events 🎸":  // Adjust this to match your button label
                    eventCommandReceived(chatId, currentPage);
                    break;
                case "professors 👩‍🏫":
                    professorCommandReceived(chatId, currentPage);
                    break;
                case "important rooms ❕":
                    importantRoomsCommandReceived(chatId, currentPage);
                    break;
                case "clubs 🏇":
                    clubCommandReceived(chatId, currentPage);
                    break;
                case "university facilities 🧘‍♂️":
                    campusFacilityCommandReceived(chatId, currentPage);
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String determiner = update.getCallbackQuery().getMessage().getText();
            if (callbackData.equals(NEXT_BUTTON)) {
                currentPage++;
                if (determiner.startsWith("Events:")) {
                    eventCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Clubs:")) {
                    clubCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Professors:")) {
                    professorCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Important rooms:")) {
                    importantRoomsCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Campus facilities:")) {
                    campusFacilityCommandReceived(chatId, currentPage);
                }
            } else if (callbackData.equals(PREV_BUTTON)) {
                if (currentPage > 0) {
                    currentPage--;
                }
                if (determiner.startsWith("Events:")) {
                    eventCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Clubs:")) {
                    clubCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Professors:")) {
                    professorCommandReceived(chatId, currentPage);
                } else if (determiner.startsWith("Campus facilities:")) {
                    campusFacilityCommandReceived(chatId, currentPage);
                }
            }


        }
    }


    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        displayMenu(message);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayMenu(SendMessage message) {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("events \uD83C\uDFB8");
        row.add("important rooms ❕");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("professors 👩‍🏫");
        row.add("clubs 🏇");
        row.add("university facilities 🧘‍♂️");

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


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    private void professorCommandReceived(long chatId, int page) {

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


    private void startCommandReceived(long chatId, String username) {
        String answer = EmojiParser.parseToUnicode("Hi, " + username + ", nice to meet you!" + " :blush:");
        log.info("Replied to user {} ", username);
        sendMessage(chatId, answer);
    }

    private void eventCommandReceived(long chatId, int page) {
        createEvent();
        var events = eventService.findAll(page, 1);

        StringBuilder messageText = new StringBuilder("Events:\n");

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
            log.info("Response Events {}", events);
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

    private void importantRoomsCommandReceived(long chatId, int page) {
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

    private void campusFacilityCommandReceived(long chatId, int page) {
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


    private void clubCommandReceived(long chatId, int page) {
        createSpanishClub();
        createArabicClub();
        createGermanClub();
        var clubsPage = clubService.findAll(page, 1);
        StringBuilder messageText = new StringBuilder("Clubs:\n");
        for (Club club : clubsPage) {
            String temple = """
                    Club name : %s
                    Description : %s
                    Contact: %s""";
            String message = String.format(temple, club.getName(), club.getDescription(), club.getContact());
            messageText.append(message).append("\n");
            log.info("Response Clubs {}", club);

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

    /// TEST clubs create
    void createSpanishClub() {
        var club = new Club("Spanish Club",
                "Bienvenido's amigos! \uD83C\uDDEA\uD83C\uDDF8 Do you want to speak and communicate with Spanish speakers? Come to our classes",
                "https://t.me/+FDo5gxtDQQowOWZi");
        clubService.addClub(club);
    }

    void createArabicClub() {
        var club = new Club("Arabic Club",
                "! \uD83C\uDF19 Want to dive into the beauty of the Arabic language? Join our fun and interactive sessions with Rubina",
                "https://t.me/arabicclubwut");
        clubService.addClub(club);
    }

    void createGermanClub() {
        var club = new Club("German Club",
                "Hallo! \uD83C\uDF0D Join Bekzod in learning German and explore one of the most widely spoken languages in Europe",
                "https://t.me/DeutschWebster");
        clubService.addClub(club);
    }
}

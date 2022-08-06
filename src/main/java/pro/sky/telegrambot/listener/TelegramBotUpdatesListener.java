package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exeptions.WrongDateBaseWork;
import pro.sky.telegrambot.exeptions.WrongParseDateExeption;
import pro.sky.telegrambot.model.ChatId;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.ChatIdRepository;
import pro.sky.telegrambot.repository.TaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TaskRepository taskRepository;
    private final ChatIdRepository chatIdRepository;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TaskRepository taskRepository, ChatIdRepository chatIdRepository) {
        this.taskRepository = taskRepository;
        this.chatIdRepository = chatIdRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /*Чтобы была именн рассылка сообщений, id чата, где пользователь активрует с помошью
     * /start бота, попадает в отдельный репозиторий и сохраняентся там. Сообщение, которое
     * нужно разослать, сохранияется в репозитории для всех чатов, где бот активирован. Подтянул
     * домашних, кого не жалко)*/
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message().text().equals("/start")) {
                SendMessage message = new SendMessage(update.message().chat().id(), "Welcome!");
                SendResponse response = telegramBot.execute(message);
                if (!chatIdRepository.existsChatIdByChatId(update.message().chat().id())) {
                    ChatId chat = new ChatId(update.message().chat().id());
                    chatIdRepository.save(chat);
                }
                if (!response.isOk()) {
                    response.errorCode();
                }
            } else {
                if (isDate(update.message().text())) {
                    List<Long> listOfId = chatIdRepository.findAll().stream()
                            .map(ChatId::getChatId).collect(Collectors.toList());
                    for (Long chatId : listOfId) {
                        NotificationTask task = new NotificationTask(chatId,
                                itemFromMessage(update.message().text()),
                                LocalDateTime.parse(dateFromMessage(update.message().text()), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                        try {
                            taskRepository.save(task);
                        } catch (WrongDateBaseWork e) {
                            System.out.println("База данных не сохранила данные");
                        }
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        logger.info("run method is working");
        if (taskRepository.existsNotificationTaskByDateOfMessage((LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))) {
            List<NotificationTask> listOfTasks = taskRepository.findAll()
                    .stream()
                    .filter(e -> e.getDateOfMessage().equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))
                    .collect(Collectors.toList());
            for (NotificationTask i : listOfTasks) {
                SendMessage message = new SendMessage(i.getChatId(), i.getBotMessage());
                SendResponse response = telegramBot.execute(message);
                if (!response.isOk()) {
                    response.errorCode();
                }
            }
        }
    }

    /*Чиобы не загромождать репозиторий, он с некоторой периодичностью очищается от старых
     * записей. Я поставил @hourly, чтобы можно было бымтро посмотреть, как метод работает,
     * но в идевле @daily или @weekly*/
    @Scheduled(cron = "@hourly")
    public void delete() {
        logger.info("delete method is working");
        if (taskRepository.count() != 0) {
            for (NotificationTask i : taskRepository.findAll()) {
                if (i.getDateOfMessage().isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
                    try {
                        taskRepository.delete(i);
                    } catch (WrongDateBaseWork e) {
                        System.out.println("База данных не удалила данные");
                    }
                }
            }
        }
    }


    private boolean isDate(String s) {
        Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    // Методы публичные, так как теоретически могут где-то ещё использоваться
    public String dateFromMessage(String s) {
        Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            throw new WrongParseDateExeption();
        }
    }

    public String itemFromMessage(String s) {
        Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            return matcher.group(3);
        } else {
            throw new WrongParseDateExeption();
        }
    }

}

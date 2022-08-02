package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue
    private Long id;

    private Long chatId;
    private String botMessage;
    private LocalDateTime dateOfMessage;

    public NotificationTask() {
    }

    public NotificationTask(long chatId, String botMessage, LocalDateTime dateOfMessage) {
        this.chatId = chatId;
        this.botMessage = botMessage;
        this.dateOfMessage = dateOfMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChat_id(long chatId) {
        this.chatId = chatId;
    }

    public String getBotMessage() {
        return botMessage;
    }

    public void setTextMessage(String botMessage) {
        this.botMessage = botMessage;
    }

    public LocalDateTime getDateOfMessage() {
        return dateOfMessage;
    }

    public void setDate(LocalDateTime dateOfMessage) {
        this.dateOfMessage = dateOfMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return chatId == that.chatId && id.equals(that.id) && botMessage.equals(that.botMessage) && dateOfMessage.equals(that.dateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, botMessage, dateOfMessage);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", botMessage='" + botMessage + '\'' +
                ", dateOfMessage=" + dateOfMessage +
                '}';
    }
}


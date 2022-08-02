package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ChatId {
    @Id
    @GeneratedValue
    private Long id;
    private Long chatId;

    public ChatId() {
    }

    public ChatId(long chatId) {
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatId chatId1 = (ChatId) o;
        return chatId == chatId1.chatId && id.equals(chatId1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId);
    }

    @Override
    public String toString() {
        return "ChatId{" +
                "id=" + id +
                ", chatId=" + chatId +
                '}';
    }
}

package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.ChatId;

@Repository
public interface ChatIdRepository extends JpaRepository<ChatId, Long> {
    boolean existsChatIdByChatId(long id);
}

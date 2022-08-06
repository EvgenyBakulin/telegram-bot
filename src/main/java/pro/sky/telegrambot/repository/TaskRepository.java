package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<NotificationTask, Integer> {

    boolean existsNotificationTaskByDateOfMessage(LocalDateTime date);

    // Optional<NotificationTask> findByDateOfMessage(LocalDateTime date);
}

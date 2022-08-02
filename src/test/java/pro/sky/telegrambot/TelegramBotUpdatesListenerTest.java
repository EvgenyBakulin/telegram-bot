package pro.sky.telegrambot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;

public class TelegramBotUpdatesListenerTest {

    private TelegramBotUpdatesListener out;

    @Test
    public void CheckParsingOfDate() {
        String s = "22.08.2022 12:12";
        Assertions.assertEquals(out.dateFromMessage("22.08.2022 12:12 Помой посуду"), s);

    }

    @Test
    public void CheckParsingOfItem() {
        String s = "Помой посуду";
        Assertions.assertEquals(out.itemFromMessage("22.08.2022 12:12 Помой посуду"), s);
    }
}

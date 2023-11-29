package ru.sigma.sigmmix.services.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class NotifierBot extends TelegramLongPollingBot {

    private String BOT_TOKEN;
    private String BOT_NAME;

    @Autowired
    public NotifierBot(@Value("${bot.token}") String BOT_TOKEN, @Value("${bot.name}") String BOT_NAME) {
        this.BOT_TOKEN = BOT_TOKEN;
        this.BOT_NAME = BOT_NAME;
    }

    /**
     * Обертка для отправки телеграм-сообщения
     * @param telegramId - идентификатор чата (клиентский telegram_id)
     * @param messageText - текст сообщения
     */
    public void sendMessage(String telegramId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(telegramId);
        message.setText(messageText);
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Не обрабатываем входящие сообщения
    }
}

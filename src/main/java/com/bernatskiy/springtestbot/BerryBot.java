package com.bernatskiy.springtestbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Optional;

public class BerryBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleMessage(Message message) throws TelegramApiException {
        //handle command
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

                switch (command) {
                    case "/set_procedure":
                        execute(SendMessage.builder()
                                .text("Please, choose the procedure you want to do.")
                                .chatId(message.getChatId().toString())
                                .build());
                        return;
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "SpringDemoBot";
    }

    @Override
    public String getBotToken() {
        return "5746743263:AAGRhNqTDDpjzI7vKRvqzqdqmbzLe-8Nr_A";
    }

    public static void main(String[] args) throws TelegramApiException {
        BerryBot bot = new BerryBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}

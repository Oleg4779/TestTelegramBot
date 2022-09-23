package com.bernatskiy.springtestbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BerryBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "BerryStudioDemoBot";
    }

    @Override
    public String getBotToken() {
        return "5746743263:AAGRhNqTDDpjzI7vKRvqzqdqmbzLe-8Nr_A";
    }

    @Override
    public void onUpdateReceived(Update update) {
//        System.out.println(update.getMessage().getText());
//        System.out.println(update.getMessage().getFrom().getFirstName());

        String command = update.getMessage().getText();

        if(command.equals("/run")) {
            String message = "You entered message run";
            SendMessage response = new SendMessage();
            response.setChatId(update.getMessage().getChatId().toString());
            response.setText(message);

            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}

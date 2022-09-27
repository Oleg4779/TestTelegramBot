package com.bernatskiy.springtestbot;

import com.bernatskiy.springtestbot.entity.Procedure;
import com.bernatskiy.springtestbot.service.ProcedureModeService;
import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BerryBot extends TelegramLongPollingBot {

    private final ProcedureModeService procedureModeService = ProcedureModeService.getInstance();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallBack(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleCallBack(CallbackQuery callbackQuery) {

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
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        List<InlineKeyboardButton> inlineKeyboardRow1 = new ArrayList<>();
                        List<InlineKeyboardButton> inlineKeyboardRow2 = new ArrayList<>();
                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
                        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
                        inlineKeyboardButton1.setText("Чистка");
                        inlineKeyboardButton1.setCallbackData("Вы выбрали процедуру \"Чистка\"");
                        inlineKeyboardRow1.add(inlineKeyboardButton1);
                        inlineKeyboardButton2.setText("Пилинг");
                        inlineKeyboardButton2.setCallbackData("Вы выбрали процедуру \"Пилинг\"");
                        inlineKeyboardRow1.add(inlineKeyboardButton2);
                        inlineKeyboardButton3.setText("Уходы");
                        inlineKeyboardButton3.setCallbackData("Вы выбрали процедуру \"Уходы\"");
                        inlineKeyboardRow2.add(inlineKeyboardButton3);
                        inlineKeyboardButton4.setText("Релакс уходы");
                        inlineKeyboardButton4.setCallbackData("Вы выбрали процедуру \"Релакс уходы\"");
                        inlineKeyboardRow2.add(inlineKeyboardButton4);
                        buttons.add(inlineKeyboardRow1);
                        buttons.add(inlineKeyboardRow2);
                        inlineKeyboardMarkup.setKeyboard(buttons);

                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text("Выберите тип процедуры:")
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                .build());

                        return;
                }
            }
        }
    }

//    private String getProcedureButton(Procedure saved, Procedure current) {
//        return saved == current ? current.getName() + "✅" : current.getName();
//    }

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

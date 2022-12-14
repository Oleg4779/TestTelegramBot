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

import java.util.*;

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
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Procedure newProcedure = Procedure.valueOf(param[1]);
        procedureModeService.setProcedure(message.getChatId(), newProcedure);
//        System.out.println(callbackQuery.getData().toString());
    }

    public void handleMessage(Message message) throws TelegramApiException {
        //handle command
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message
                        .getText()
                        .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

                switch (command) {
                    case "/set_procedure":
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        Procedure procedure = procedureModeService.getProcedure(message.getChatId());
                        for (Procedure procedureType : Procedure.values()) {
                            buttons.add(
                                    Arrays.asList(
                                            InlineKeyboardButton.builder()
                                                    .text(procedureType.getName())
                                                    .callbackData("Procedure:" + procedure.name())
                                                    .build()));
                        }

                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text("???????????????? ?????? ??????????????????:")
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                .build());

                        break;
                }
            }
        } else {
            SendMessage replyToUser = new SendMessage();
            replyToUser.setChatId(message.getChatId().toString());
            replyToUser.setText("Command was not recognized");
            execute(replyToUser);
        }
    }

//    private String getProcedureButton(Procedure saved, Procedure current) {
//        return saved == current ? saved.getName() + "???" : current.getName();
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

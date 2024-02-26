package edu.bot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

import java.io.IOException;

import static edu.bot.utils.MessageUtils.generateSendMessageWithText;


@Component
@Log4j
    public class TelegramBot extends TelegramLongPollingBot {
        @Value("${bot.name}")
        private String botName;
        @Value("${bot.token}")
        private String botToken;
        //private static final Logger log = Logger.getLogger(String.valueOf(TelegramBot.class));
        private UpdateController updateController;

        public TelegramBot(UpdateController updateController){
            this.updateController = updateController;
        }

        @PostConstruct
        public void init(){
            updateController.registerBot(this);
        }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        System.out.println(message.getText());
        log.debug(message.getText());
        try {
            updateController.processUpdate(update);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //TODO: как-то оптимизировать эти два метода
    public void sendAnswerMessage(SendMessage message){
            if (message != null){
                try{
                    execute(message);
                } catch (TelegramApiException e){
                    log.error(e);
                    System.err.println(e.getMessage());
                }
            }
    }

    public void sendAnswerMessage(SendPhoto photo){
        if (photo != null){
            try{
                execute(photo);
            } catch (TelegramApiException e){
                log.error(e);
                System.err.println(e.getMessage());
            }
        }
    }
}


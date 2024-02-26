package edu.bot.controller;

import edu.bot.model.ImageProcessing;
import edu.bot.service.MainServiceImpl;
import edu.bot.utils.MessageUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static edu.bot.utils.MessageUtils.generateSendMessageWithText;
import static edu.bot.utils.MessageUtils.generateSendPhotoWithText;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;

    @Autowired
    private ImageProcessing imageProcessing;

    @Autowired
    private MainServiceImpl mainService;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) throws IOException {
        if (update == null) {
            log.error("Received update is null");
            return;
        }
        if (update.getMessage() != null) {
            distributeMessageByType(update);
        } else {
            log.error("Received unsupported message type " + update);
        }
    }

    private void distributeMessageByType(Update update) throws IOException {
        Message message = update.getMessage();
        if (message.getText() != null) {
            processTextMessage(update);
        } else if (message.getPhoto() != null) {
            processPhotoMessage(update);
        } else {
            processUnsupportedMessage(update);
        }
    }

    private void processUnsupportedMessage(Update update) {
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update, "Неподдерживаемый тип");
        setView(sendMessage);
    }

    private void processPhotoMessage(Update update) throws IOException {
        SendMessage sendMessage = MessageUtils.generateSendMessageWithText(update, "Это фото");
        mainService.processPhotoMessage(update);
        byte[] newImage = imageProcessing.processImage();
        setView(sendMessage);
        setView(generateSendPhotoWithText(update, newImage));
    }

    private void processTextMessage(Update update) {
        SendMessage sendMessage = generateSendMessageWithText(update, "Это текст");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void setView(SendPhoto sendPhoto) {
        telegramBot.sendAnswerMessage(sendPhoto);
    }
}

package edu.bot.service;

import edu.bot.controller.TelegramBot;
import edu.bot.entity.AppPhoto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static edu.bot.utils.MessageUtils.generateSendMessageWithText;

@Log4j
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService{
    @Autowired
    private final FileService fileService;

    @Override
    public void processPhotoMessage(Update update) {
        var chatId = update.getMessage().getChatId();

        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String answer = "Фото успешно загружено для обработки!";
            generateSendMessageWithText(update, answer);
            //ByteArrayToImage(photo);
        } catch (RuntimeException e) {
            log.error(e);
            String error = "К сожалению, загрузка фото не удалась. Повторите попытку позже.";
            generateSendMessageWithText(update, error);
            System.err.println(error);
        }
    }

    public void ByteArrayToImage (byte[] byteArrayImage) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArrayImage);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, "jpg", new File("/Users/abalonef/Desktop/imageFromTg.jpg"));
        System.out.println("image created");
    }
}

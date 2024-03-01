package edu.bot.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class MessageUtils {
    public static SendMessage generateSendMessageWithText(Update update, String text) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        return sendMessage;
    }

    public static SendPhoto generateSendPhotoWithText(Update update, byte[] binarImage) throws IOException {
        File imageFile = createTempFileFromBytes(binarImage);
        Message message = update.getMessage();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId().toString());
        sendPhoto.setPhoto(new InputFile(imageFile));
        return sendPhoto;
    }

    private static File createTempFileFromBytes(byte[] bytes) throws IOException {
        File tempFile = File.createTempFile("temp_image", ".jpeg");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(bytes);
        }
        return tempFile;
    }

}

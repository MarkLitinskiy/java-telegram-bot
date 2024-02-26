package edu.bot.service;

import edu.bot.entity.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppPhoto processPhoto(Message telegramMessage);
}

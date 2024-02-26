package edu.bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {
    void processPhotoMessage(Update update);
}

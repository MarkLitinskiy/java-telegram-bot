package edu.bot.service;

import edu.bot.dao.AppPhotoDAO;
import edu.bot.dao.BinaryContentDAO;
import edu.bot.entity.AppPhoto;

import edu.bot.entity.BinaryContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Log4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    @Value("${bot.token}")
    private String token;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    private final AppPhotoDAO appPhotoDAO;
    private final BinaryContentDAO binaryContentDAO;

    @Override
    public AppPhoto processPhoto(Message telegramMessage) {
        PhotoSize telegramPhoto = telegramMessage.getPhoto().get(3);
        String fileId = telegramPhoto.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK){
            JSONObject jsonObject = new JSONObject(response.getBody());
            String filePath = String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
            byte[] fileInByte = downloadFile(filePath);
            BinaryContent transientBinaryContent = BinaryContent.builder().photoAsArrayOfBytesOriginal(fileInByte).build();
            BinaryContent persistentBinaryContent = binaryContentDAO.save(transientBinaryContent);
            AppPhoto transientAppPhoto = buildTransientAppPhoto(telegramPhoto, persistentBinaryContent);
            return appPhotoDAO.save(transientAppPhoto);
        } else{
            System.err.println("При скачивании что-то пошло не так((");
        }
        return null;
    }

    private AppPhoto buildTransientAppPhoto(PhotoSize telegramPhoto, BinaryContent persistentBinaryContent){
        return AppPhoto.builder()
                .telegramFieldId(telegramPhoto.getFileId())
                .binaryContent(persistentBinaryContent)
                .fileSize(telegramPhoto.getFileSize())
                .build();
    }

    private ResponseEntity<String> getFilePath(String fileId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri, HttpMethod.GET, request, String.class, token, fileId
        );
    }

    private byte[] downloadFile(String filePath){
        String fullUri = fileStorageUri.replace("{token}", token).replace("{filePath}", filePath);
        URL urlObj = null;
        try{
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try(InputStream is = urlObj.openStream()){
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}

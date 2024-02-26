package edu.bot.model;

import edu.bot.dao.AppPhotoDAO;
import edu.bot.dao.BinaryContentDAO;
import edu.bot.dao.MemTextDAO;
import edu.bot.entity.BinaryContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.*;
import java.awt.font.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.List;
import java.util.ArrayList;
@Component
@Log4j
@Service
@RequiredArgsConstructor
public class ImageProcessing {
    BufferedImage image;
    Font font = new Font("Arial", Font.BOLD, 40);
    AttributedString attributedText;
    Graphics graphics;
    String text;

    List<String> listOfString = new ArrayList<>();

    int countStr = 0;

    @Autowired
    private MemTextDAO memTextDAO;
    @Autowired
    private BinaryContentDAO binaryContentDAO;
    @Autowired
    private AppPhotoDAO appPhotoDAO;
    private int lineHeight;

    //TODO: обнулить все переменные перед вызовом
    public byte[] processImage() throws IOException {
        // Создаем объект PageRequest для запроса одной страницы с одной записью (последней)
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"));
        Long id = appPhotoDAO.findAll(pageRequest).stream().findFirst().get().getId();
        this.text = memTextDAO.findById(RandomRange.rnd()).get().getText_data();
        image = ByteArrayToImage(binaryContentDAO.findById(id).get().getPhotoAsArrayOfBytesOriginal());
        graphics = image.getGraphics();
        adaptatingFont();
        drawText();
        return saveImage(id);
    }

    public BufferedImage ByteArrayToImage (byte[] byteArrayImage) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArrayImage);
        BufferedImage bImage2 = ImageIO.read(bis);
        //ImageIO.write(bImage2, "jpg", new File("/Users/abalonef/Desktop/imageFromTg.jpg"));
        //System.out.println("image created");
        return bImage2;
    }

    public byte[] ByteArrayToImage (BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        return baos.toByteArray();
    }

    private void adaptatingFont(){
        String strText = text;
        FontMetrics ruler = graphics.getFontMetrics(font);
        GlyphVector vector;

        Shape outline;

        double expectedWidth;
        double expectedHeight;

        do{
            vector = font.createGlyphVector(ruler.getFontRenderContext(), strText);
            outline = vector.getOutline(0, 0);
            expectedWidth = outline.getBounds().getWidth();
            expectedHeight = outline.getBounds().getHeight();
            countStr++;
            strText = strText.substring(0, strText.length() / 2);
        }while(image.getWidth() < expectedWidth || image.getHeight() < expectedHeight);

        // Рассчитываем длину каждой части
        int partLength = (int) Math.ceil((double) text.length() / countStr);

        // Разделяем строку на части и добавляем в список
        for (int i = 0; i < countStr; i++) {
            int start = i * partLength;
            int end = Math.min(start + partLength, text.length());
            listOfString.add(text.substring(start, end));
        }
        lineHeight = ruler.getHeight();
    }


    public void drawText() {
        FontMetrics metrics = graphics.getFontMetrics(font);
        int positionX;
        int positionY = image.getHeight() - (countStr * lineHeight) - 20;

        for (String line : listOfString) {
            positionX = (image.getWidth() - metrics.stringWidth(line)) / 2;
            attributedText = new AttributedString(line);
            attributedText.addAttribute(TextAttribute.FONT, font);
            attributedText.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
            attributedText.addAttribute(TextAttribute.BACKGROUND, Color.BLACK);
            graphics.drawString(attributedText.getIterator(), positionX, positionY);
            positionY += lineHeight; // Переход на следующую строку
        }

        //graphics.dispose();
    }

    public byte[] saveImage(Long id) throws IOException {
        // Получаем объект из базы данных по его id
        BinaryContent entity = binaryContentDAO.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

        byte[] binarNewImage = ByteArrayToImage(image);
        // Вносим изменения в поле объекта
        entity.setPhotoAsArrayOfBytesAfterProcessing(binarNewImage);

        // Сохраняем обновленный объект в базе данных
        binaryContentDAO.save(entity);
        return binarNewImage;
    }
}

package edu.bot;

import edu.bot.model.ImageProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DispatcherApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(DispatcherApplication.class);
//        ImageProcessing imageProcessing = new ImageProcessing("/Users/abalonef/Desktop/test.jpg", "Если я женюсь, то это, скорее всего, будет женщина");
//        imageProcessing.drawText();
//        imageProcessing.saveImage("/Users/abalonef/Desktop/testImage.jpg");
    }
}

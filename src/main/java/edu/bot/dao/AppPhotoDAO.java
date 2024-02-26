package edu.bot.dao;

import edu.bot.entity.AppPhoto;
import edu.bot.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoDAO extends JpaRepository<BinaryContent, Long> {
    AppPhoto save(AppPhoto transientAppPhoto);
}

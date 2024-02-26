package edu.bot.dao;

import edu.bot.entity.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
    BinaryContent save(BinaryContent transientBinaryContent);
    Optional<BinaryContent> findById(Long id);

}

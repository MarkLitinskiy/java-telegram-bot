package edu.bot.dao;

import edu.bot.entity.MemText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemTextDAO extends JpaRepository<MemText, Long> {
    MemText save(MemText transientMemText);
    Optional<MemText> findById(Long id);
}

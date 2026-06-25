package karol.studia.mas_projekt_koncowy.repository;

import karol.studia.mas_projekt_koncowy.model.AudioContent;
import karol.studia.mas_projekt_koncowy.model.ContentStatus;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AudioContentRepository extends CrudRepository<AudioContent, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT a FROM AudioContent a LEFT JOIN FETCH a.playlistEntries WHERE a.id = :id")
    Optional<AudioContent> findByIdWithPlaylistEntries(@org.springframework.data.repository.query.Param("id") Long id);

    List<AudioContent> findByStatus(ContentStatus contentStatus);
}

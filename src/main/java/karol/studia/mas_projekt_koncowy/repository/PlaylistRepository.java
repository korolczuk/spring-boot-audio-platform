package karol.studia.mas_projekt_koncowy.repository;

import karol.studia.mas_projekt_koncowy.model.Playlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaylistRepository extends CrudRepository<Playlist, Long> {

    @Query("SELECT p FROM Playlist p LEFT JOIN FETCH p.playlistEntry pe LEFT JOIN FETCH pe.audioContent WHERE p.id = :id")
    Optional<Playlist> findByIdWithEntries(@Param("id") Long id);
}

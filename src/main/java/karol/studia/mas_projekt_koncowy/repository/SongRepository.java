package karol.studia.mas_projekt_koncowy.repository;

import karol.studia.mas_projekt_koncowy.model.Song;
import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<Song, Long> {
}

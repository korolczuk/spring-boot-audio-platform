package karol.studia.mas_projekt_koncowy.repository;

import karol.studia.mas_projekt_koncowy.model.MusicCreator;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MusicCreatorRepository extends CrudRepository<MusicCreator, Long> {

    Optional<MusicCreator> findByUserName(String userName);
}

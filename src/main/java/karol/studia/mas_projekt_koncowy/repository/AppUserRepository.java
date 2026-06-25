package karol.studia.mas_projekt_koncowy.repository;

import karol.studia.mas_projekt_koncowy.model.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByUserName(String userName);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.playlists WHERE u.userName = :userName")
    Optional<AppUser> findByUserNameWithPlaylists(@Param("userName") String userName);
}


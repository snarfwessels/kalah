package eu.snarf.kalah.repositories;

import eu.snarf.kalah.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Frans on 3/19/2018.
 */
@Repository
public interface PlayerRepo extends JpaRepository<Player, Long> {

    Player findByName(final String name);
}

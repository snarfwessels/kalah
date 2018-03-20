package eu.snarf.kalah.repositories;

import eu.snarf.kalah.model.Kalah;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Frans on 3/19/2018.
 */
@Repository
public interface KalahRepo extends JpaRepository<Kalah, Long> {
}

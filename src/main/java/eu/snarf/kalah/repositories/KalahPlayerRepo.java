package eu.snarf.kalah.repositories;

import eu.snarf.kalah.model.KalahPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Frans on 3/19/2018.
 */
@Repository
public interface KalahPlayerRepo extends JpaRepository<KalahPlayer, Long> {

    List<KalahPlayer> findByKalah(Long kalahId);
}

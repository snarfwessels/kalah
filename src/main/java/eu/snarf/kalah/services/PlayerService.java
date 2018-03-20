package eu.snarf.kalah.services;

import eu.snarf.kalah.model.Player;
import eu.snarf.kalah.repositories.PlayerRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service which handles the actions for a player.
 *
 * @author Frans on 3/19/2018.
 */
@Service
public class PlayerService {

    private final PlayerRepo playerRepo;

    public PlayerService(final PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public Player getPlayer(final String name){
        return playerRepo.findByName(name);
    }

    @Transactional
    public Player createPlayer(final String name){
        return playerRepo.save(Player.builder().name(name).build());
    }
}

package eu.snarf.kalah.transformers;

import eu.snarf.kalah.model.Player;
import eu.snarf.kalah.model.dto.PlayerDto;
import eu.snarf.kalah.repositories.PlayerRepo;
import org.springframework.stereotype.Component;

/**
 * @author Frans on 3/19/2018.
 */
@Component
public class PlayerDtoToEntity implements Transformer<PlayerDto, Player> {

    private final PlayerRepo playerRepo;

    public PlayerDtoToEntity(final PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @Override
    public Player apply(final PlayerDto playerDto) {
        Player player = playerRepo.findByName(playerDto.getName());
        if(player == null){
            player = Player.builder().name(playerDto.getName()).build();
        }
        return player;
    }
}

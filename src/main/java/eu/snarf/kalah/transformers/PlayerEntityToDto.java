package eu.snarf.kalah.transformers;

import eu.snarf.kalah.model.Player;
import eu.snarf.kalah.model.dto.PlayerDto;
import org.springframework.stereotype.Component;

/**
 * @author Frans on 3/19/2018.
 */
@Component
public class PlayerEntityToDto implements  Transformer<Player, PlayerDto> {

    @Override
    public PlayerDto apply(final Player player) {
        final PlayerDto dto = PlayerDto.builder()
                .name(player.getName())
                .build();
        return dto;
    }
}

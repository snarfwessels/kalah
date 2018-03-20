package eu.snarf.kalah.transformers;

import eu.snarf.kalah.model.KalahPlayer;
import eu.snarf.kalah.model.dto.PlayerDto;
import org.springframework.stereotype.Component;

/**
 * @author Frans on 3/20/2018.
 */
@Component
public class KalahPlayerEntityToPlayerDto implements Transformer<KalahPlayer, PlayerDto> {

    private final PlayerEntityToDto playerEntityToDto;

    public KalahPlayerEntityToPlayerDto(final PlayerEntityToDto playerEntityToDto) {
        this.playerEntityToDto = playerEntityToDto;
    }

    @Override
    public PlayerDto apply(final KalahPlayer kalahPlayer) {
        PlayerDto dto = playerEntityToDto.apply(kalahPlayer.getPlayer());
        dto.setSide(kalahPlayer.getSide());
        return dto;
    }
}

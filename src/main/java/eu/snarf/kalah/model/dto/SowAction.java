package eu.snarf.kalah.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Actionb to do sow a turn in a game.
 * GameId and chosen Pit should be provided
 *
 * @author Frans on 3/20/2018.
 */
@NoArgsConstructor

@Getter
public class SowAction implements Serializable {
    private long gameId;
    private int fromPit;

    @Builder
    private SowAction(final long gameId, final int fromPit) {
        this.gameId = gameId;
        this.fromPit = fromPit;
    }
}

package eu.snarf.kalah.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.snarf.kalah.exceptions.Messages;
import eu.snarf.kalah.model.Side;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Frans on 3/19/2018.
 */
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class KalahDto implements Serializable{
    private Long id;
    private PlayerDto playerOne;
    private PlayerDto playerTwo;
    private List<PitDto> pits;
    private Side activeSide;
    private boolean ended;
    private Messages messages = new Messages();

    @Builder
    private KalahDto(final Long id, final PlayerDto playerOne, final PlayerDto playerTwo,
                     final List<PitDto> pits, final Side activeSide, final boolean ended,final Messages messages) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.pits = pits;
        this.activeSide = activeSide;
        this.ended = ended;
        this.messages = messages;
    }
}

package eu.snarf.kalah.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Frans on 3/20/2018.
 */
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StartAction implements Serializable {

    private PlayerDto playerOne;
    private PlayerDto playerTwo;

    @Builder
    private StartAction(final PlayerDto playerOne, final PlayerDto playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }
}

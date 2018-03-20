package eu.snarf.kalah.model.dto;

import eu.snarf.kalah.model.Side;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Frans on 3/19/2018.
 */
@NoArgsConstructor

@Getter
public class PlayerDto {
    private String name;
    private Side side;

    @Builder
    private PlayerDto(final String name, final Side side) {
        this.name = name;
        this.side = side;
    }

    public void setSide(final Side side){
        this.side = side;
    }
}

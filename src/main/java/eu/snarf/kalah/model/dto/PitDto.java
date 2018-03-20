package eu.snarf.kalah.model.dto;

import eu.snarf.kalah.model.PitType;
import eu.snarf.kalah.model.Side;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Frans on 3/20/2018.
 */
@NoArgsConstructor

@Getter
public class PitDto {

    private PitType pitType;
    private int pitOrder;
    private int seeds;
    private Side side;

    @Builder
    private PitDto(final PitType pitType, final int pitOrder, final int seeds, final Side side) {
        this.pitType = pitType;
        this.pitOrder = pitOrder;
        this.seeds = seeds;
        this.side = side;
    }
}

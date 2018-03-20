package eu.snarf.kalah.transformers;

import eu.snarf.kalah.model.Pit;
import eu.snarf.kalah.model.dto.PitDto;
import org.springframework.stereotype.Component;

/**
 * @author Frans on 3/20/2018.
 */
@Component
public class PitEntityToDto implements Transformer<Pit, PitDto>{

    @Override
    public PitDto apply(final Pit pit) {
        return PitDto.builder()
                .pitOrder(pit.getPitOrder())
                .pitType(pit.getPitType())
                .seeds(pit.getSeeds())
                .side(pit.getSide())
                .build() ;
    }
}

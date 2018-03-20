package eu.snarf.kalah.transformers;

import eu.snarf.kalah.model.Kalah;
import eu.snarf.kalah.model.dto.KalahDto;
import eu.snarf.kalah.model.dto.PitDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frans on 3/19/2018.
 */
@Component
public class KalahEntityToDto implements Transformer<Kalah, KalahDto> {

    private final PitEntityToDto pitEntityToDto;
    private final KalahPlayerEntityToPlayerDto kalahPlayerEntityToPlayerDto;

    public KalahEntityToDto(final PitEntityToDto pitEntityToDto,
                            final KalahPlayerEntityToPlayerDto kalahPlayerEntityToPlayerDto) {
        this.pitEntityToDto = pitEntityToDto;
        this.kalahPlayerEntityToPlayerDto = kalahPlayerEntityToPlayerDto;
    }

    @Override
    public KalahDto apply(final Kalah kalah) {
        List<PitDto> pits = new ArrayList<>();
        kalah.getPits().forEach(en -> pits.add(pitEntityToDto.apply(en)));

        return KalahDto.builder()
                .id(kalah.getId())
                .playerOne(kalahPlayerEntityToPlayerDto.apply(kalah.getPlayers().get(0)))
                .playerTwo(kalahPlayerEntityToPlayerDto.apply(kalah.getPlayers().get(1)))
                .pits(pits)
                .activeSide(kalah.getActiveSide())
                .messages(kalah.getMessages())
                .ended(kalah.isEnded())
                .build();
    }
}

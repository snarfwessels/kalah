package eu.snarf.kalah.transformers;

import eu.snarf.kalah.model.Kalah;
import eu.snarf.kalah.model.Side;
import eu.snarf.kalah.model.dto.KalahDto;
import eu.snarf.kalah.repositories.KalahRepo;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Frans on 3/19/2018.
 */
@Component
public class KalahDtoToEntity implements Transformer<KalahDto, Kalah>{

    private final KalahRepo kalahRepo;

    public KalahDtoToEntity(final KalahRepo kalahRepo) {
        this.kalahRepo = kalahRepo;
    }

    @Override
    public Kalah apply(final KalahDto kalahDto) {
        Optional<Kalah> optional = kalahRepo.findById(kalahDto.getId());
        Kalah kalah;
        if(optional.isPresent()){
            kalah = optional.get();
        } else {
            kalah = new Kalah();
            kalah.setActiveSide(Side.NORTH);
        }
        return kalah;
    }
}

package eu.snarf.kalah.rest;

import com.sun.javafx.tools.packager.Log;
import eu.snarf.kalah.exceptions.KalahException;
import eu.snarf.kalah.model.*;
import eu.snarf.kalah.model.dto.KalahDto;
import eu.snarf.kalah.model.dto.SowAction;
import eu.snarf.kalah.model.dto.StartAction;
import eu.snarf.kalah.services.KalahService;
import eu.snarf.kalah.transformers.KalahEntityToDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Main rest controller which handles the rest-request for a game of Kalah.
 *
 * @author Frans on 3/19/2018.
 */
@Slf4j
@RestController
@RequestMapping("kalah")
public class KalahController {

    private final KalahService kalahService;
    private final KalahEntityToDto kalahEntityToDto;

    public KalahController(final KalahService kalahService, final KalahEntityToDto kalahEntityToDto) {
        this.kalahService = kalahService;
        this.kalahEntityToDto = kalahEntityToDto;
    }

    /**
     * Format of the json:
     * <code>{"playerOne":{"name": "Pietje", "side": "SOUTH"},"playerTwo":{"name": "Jantje", "side": "NORTH"}}</code>
     *
     * Method: POST
     *
     * @param startAction names of the players
     * @return startData
     */
    @PostMapping
    public @ResponseBody KalahDto startKalah(@RequestBody final StartAction startAction){
        log.debug("startAction: {}", startAction);
        final Kalah kalah = kalahService.startNewGame(startAction);
        return kalahEntityToDto.apply(kalah);
    }

    /**
     * NORTH can begin (example would be "Pietje")
     *
     * Format of the json:
     * <code>{"gameId": 1,"fromPit": 8}</code>
     *
     * Method: PUT
     *
     * @param sowAction sowing step
     * @return updated game
     */
    @PutMapping
    public @ResponseBody KalahDto sow(@RequestBody final SowAction sowAction) {
        log.debug("sowAction: {}", sowAction);
        int fromPit = sowAction.getFromPit();
        if(fromPit> 14 || fromPit < 1){
            throw new KalahException("the input pit value is not in valid range 1 till 14");
        }
        Kalah kalah = kalahService.sow(sowAction.getGameId(), fromPit);
        return kalahEntityToDto.apply(kalah);
    }

    @GetMapping
    public @ResponseBody List<KalahDto> getAllGames(){
        List<KalahDto> games = new ArrayList<>();
        kalahService.getGames().forEach(g -> games.add(kalahEntityToDto.apply(g)));
        return games;
    }

    @GetMapping("/{id}")
    public @ResponseBody KalahDto getGame(@PathVariable("id") final long gameId){
        log.debug("gameId: {}", gameId);
        return  kalahEntityToDto.apply(kalahService.getGame(gameId));
    }
}

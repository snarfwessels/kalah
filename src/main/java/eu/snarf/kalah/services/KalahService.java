package eu.snarf.kalah.services;

import eu.snarf.kalah.exceptions.KalahException;
import eu.snarf.kalah.exceptions.Messages;
import eu.snarf.kalah.model.*;
import eu.snarf.kalah.model.dto.PlayerDto;
import eu.snarf.kalah.model.dto.StartAction;
import eu.snarf.kalah.repositories.KalahRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service which handles the two main methods of the game.
 * the initialization and the sowing of seeds.
 *
 * @author Frans on 3/19/2018.
 */
@Slf4j
@Service
public class KalahService {

    private static final int NUMBER_OF_PITS = 14;

    private final KalahRepo kalahRepo;
    private final PlayerService playerService;
    private final KalahValidationService kalahValidationService;


    public KalahService(final KalahRepo kalahRepo, final PlayerService playerService,
                        final KalahValidationService kalahValidationService) {
        this.kalahRepo = kalahRepo;
        this.playerService = playerService;
        this.kalahValidationService = kalahValidationService;
    }

    /**
     * Method starts a new game. Generates the board with the pitts and when players are not present stores them
     * in a datastore.
     *
     * @param startAction The initiation of a game
     * @return a game of Kalah
     */
    @Transactional
    public Kalah startNewGame(final StartAction startAction){
        Player playerOne = getPlayer(startAction.getPlayerOne());
        Player playerTwo = getPlayer(startAction.getPlayerTwo());

        final Kalah kalah = new Kalah();

        List<KalahPlayer> players = new ArrayList<>();

        players.add(KalahPlayer.builder().kalah(kalah).player(playerOne).side(Side.NORTH).build());
        players.add(KalahPlayer.builder().kalah(kalah).player(playerTwo).side(Side.SOUTH).build());

        kalah.setPits(createStartingPits(kalah));
        kalah.setActiveSide(Side.NORTH);
        kalah.setPlayers(players);

        log.debug("new kalah: {}", kalah);

        return kalahRepo.save(kalah);
    }

    private Player getPlayer(final PlayerDto playerDto) {
        Player player = playerService.getPlayer(playerDto.getName());
        if(player == null){
            player = playerService.createPlayer(playerDto.getName());
        }
        return player;
    }

    /**
     * Get a game from the database.
     *
     * @param id of the kalah game
     * @return a game with all the data.
     */
    public Kalah getGame(final long id){
        return retrieveGame(id);
    }

    /**
     * Get all the games from the database.
     *
     * @return a game with all the data.
     */
    public List<Kalah> getGames(){
        return kalahRepo.findAll();
    }

    private Kalah retrieveGame(final long id) {
        if(kalahRepo.findById(id).isPresent()){
            return kalahRepo.findById(id).get();
        }
        throw new KalahException("Game could not be found");
    }

    private List<Pit> createStartingPits(final Kalah kalah){
        final List<Pit> pits = new ArrayList<>();
        for(int count = 1; count < NUMBER_OF_PITS +1; count++){
            Side side;
            int stones = 6;
            PitType pitType = PitType.HOUSE;
            if(count < 8) {
               side = Side.SOUTH;
            } else {
                side = Side.NORTH;
            }
            if(count == 7 || count == 14){
                pitType = PitType.STORE;
                stones = 0;
            }
            pits.add(Pit.builder()
                    .pitOrder(count)
                    .seeds(stones)
                    .side(side)
                    .pitType(pitType)
                    .kalah(kalah)
                    .build());
        }
        return pits;
    }

    /**
     * Main method for sowing seeds over the different pits
     *
     * @param kalahId the game for which to sow
     * @param fromPit  from which to start sowing
     * @return the result of the sow
     */
    @Transactional
    public Kalah sow(final long kalahId, final int fromPit) {
        final Kalah kalah = getGame(kalahId);
        final Pit startingPit =  kalah.getPitForOrder(fromPit);

        final Messages messages = kalah.getMessages();

        kalahValidationService.isItYourTurn(kalah, startingPit);
        kalahValidationService.isValidSowingPit(startingPit, messages);

        if(messages.hasErrors()){
            return kalah;
        }

        startSowing(kalah, startingPit);

        if(kalah.isGameFinished()){
            kalah.determineWinner();
            kalah.setEnded(true);
        }

        log.debug("updated kalah: {}", kalah);

        return kalahRepo.save(kalah);
    }

    /*
    Has become a bit long and difficult.
     */
    private void startSowing(final Kalah kalah, final Pit startingPit) {
        final int numberOfStonesToSow = startingPit.getSeeds();
        int sowingPitOrder = startingPit.getPitOrder();
        Side activePlayer = kalah.getActiveSide();
        boolean isLastPitEmpty = false;
        boolean isLastPitStore = false;
        for(int stones = numberOfStonesToSow; stones > 0; stones--){
            sowingPitOrder = sowingPitOrder+1;
            if(sowingPitOrder > 14){
                sowingPitOrder = 1;
            }
            Pit pit = kalah.getPitForOrder(sowingPitOrder);
            if(pit.isStore() && !pit.isFromActivePlayer(activePlayer)){
                sowingPitOrder = sowingPitOrder +1;
                pit = kalah.getPitForOrder(sowingPitOrder);
            }
            pit.addSeed(1);

            if(stones == 1 && pit.isFromActivePlayer(activePlayer)){
                if(pit.isStore()){
                    isLastPitStore = true;
                } else if (pit.getSeeds() == 0){
                    isLastPitEmpty = true;
                }
            }
        }
        startingPit.addSeed(-numberOfStonesToSow);
        if(isLastPitEmpty){
            Pit lastPit = kalah.getPitForOrder(sowingPitOrder);
            handleLastPitIsEmpty(kalah, lastPit);
        }

        if(!isLastPitStore){
            handleChangePlayer(kalah);
        }
    }

    private void handleLastPitIsEmpty(final Kalah kalah, final Pit lastPit) {
        int pitOrder = lastPit.getPitOrder();
        int oppositePitOrder = 14 - pitOrder;
        final Pit opposite = kalah.getPitForOrder(oppositePitOrder);

        int grabSeeds = lastPit.getSeeds() + opposite.getSeeds();

        lastPit.emptyPit();
        opposite.emptyPit();

        Pit ownStore = kalah.getOwnStore(kalah.getActiveSide());
        ownStore.addSeed(grabSeeds);
    }

    private void handleChangePlayer(final Kalah kalah) {
        if(Side.NORTH == kalah.getActiveSide()) {
            kalah.setActiveSide(Side.SOUTH);
        } else {
            kalah.setActiveSide(Side.NORTH);
        }
    }
}

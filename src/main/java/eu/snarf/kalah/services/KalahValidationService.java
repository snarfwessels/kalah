package eu.snarf.kalah.services;

import eu.snarf.kalah.exceptions.Messages;
import eu.snarf.kalah.model.Kalah;
import eu.snarf.kalah.model.Pit;
import org.springframework.stereotype.Service;

/**
 * Some validations which prevent a player from cheating.
 *
 * @author Frans on 3/20/2018.
 */
@Service
public class KalahValidationService {

    /**
     * Checks if it is your turn to sow.
     *
     * @param kalah active game
     * @param startingPit sowingPit
     */
    public void isItYourTurn(final Kalah kalah, final Pit startingPit) {
        if(startingPit.getSide() != kalah.getActiveSide()){
            kalah.getMessages().addErrorMessage("Sorry, it is not your turn");
        }
    }

    /**
     * Validates if you are sowing from a valid pit.
     *
     * @param startingPit sowingPit
     * @param messages messages
     */
    public void isValidSowingPit(final Pit startingPit, final Messages messages) {
        if(startingPit.getSeeds() == 0){
            messages.addErrorMessage("There are no seeds in your pit.");
        }
        if(startingPit.isStore()){
            messages.addErrorMessage("You can not sow from a 'Store' pit.");
        }
    }
}

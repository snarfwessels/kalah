package eu.snarf.kalah;


import eu.snarf.kalah.exceptions.Messages;
import eu.snarf.kalah.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Frans on 3/20/2018.
 */
public class TestData {

    public static Kalah createInitialKalah() {
        final Kalah kalah = new Kalah();
        final List<KalahPlayer> players = new ArrayList<>();
        players.add(KalahPlayer.builder().build());
        players.add(KalahPlayer.builder().build());
        kalah.setPlayers(players);

        final List<Pit> pits = new ArrayList<>();
        for(int count = 1; count < 14 +1; count++){
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

        kalah.setPits(pits);
        kalah.setActiveSide(Side.NORTH);
        kalah.setMessages(new Messages());
        return kalah;
    }

}
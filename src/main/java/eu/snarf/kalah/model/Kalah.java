package eu.snarf.kalah.model;

import eu.snarf.kalah.exceptions.Messages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.EnumType.STRING;

/**
 * @author Frans on 3/19/2018.
 */
@NoArgsConstructor

@ToString

@Getter
@Setter
@Entity
public class Kalah extends BaseEntity implements Serializable {

    @Enumerated(STRING)
    private Side activeSide;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            mappedBy = "kalah")
    private List<Pit> pits;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy="kalah",
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<KalahPlayer> players;

    @Transient
    private Messages messages = new Messages();

    private boolean ended;


    public  List<Pit> getPits(){
        pits.sort(Comparator.comparingInt(Pit::getPitOrder));
        return pits;
    }

    public Pit getOwnStore(final Side side){
        List<Pit> result = getPits().stream()
                .filter(pit -> pit.isFromActivePlayer(side))
                .filter(pit -> pit.isStore())
                .collect(Collectors.toList());
        return result.get(0);
    }

    public Pit getPitForOrder(final int order){
        return getPits().get(order -1);
    }

    public boolean isGameFinished() {
        List<Pit> resultNorth = getPits().stream()
                .filter(pit -> pit.isFromActivePlayer(Side.NORTH))
                .filter(pit -> !pit.isStore())
                .filter(pit -> pit.getSeeds() != 0)
                .collect(Collectors.toList());

        List<Pit> resultSouth = getPits().stream()
                .filter(pit -> pit.isFromActivePlayer(Side.SOUTH))
                .filter(pit -> !pit.isStore())
                .filter(pit -> pit.getSeeds() != 0)
                .collect(Collectors.toList());

        return resultNorth.isEmpty() || resultSouth.isEmpty();
    }

    public void determineWinner(){
        final int northCount = countSide(Side.NORTH);
        final int southCount = countSide(Side.SOUTH);

        final Side winner = northCount > southCount ? Side.NORTH : Side.SOUTH;
        messages.addInfoMessage("And the winnar is "+winner);
    }

    private int countSide(final Side side) {
        List<Pit> southPits = getPits().stream().filter(pit -> pit.isFromActivePlayer(side)).collect(Collectors.toList());
        int count = 0;
        for (Pit pit : southPits) {
            count = count + pit.getSeeds();
        }
        return count;
    }
}

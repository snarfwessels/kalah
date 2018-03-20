package eu.snarf.kalah.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.EnumType.STRING;

/**
 * @author Frans on 3/19/2018.
 */
@Getter
@Setter

@Entity
@Table(name = "kalah_player")
public class KalahPlayer extends BaseEntity implements Serializable {

    public KalahPlayer(){}

    @Builder
    public KalahPlayer(final Kalah kalah, final Player player, final Side side) {
        this.kalah = kalah;
        this.player = player;
        this.side = side;
    }

    @ManyToOne
    @JoinColumn(name = "kalah_id")
    private Kalah kalah;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @Enumerated(STRING)
    private Side side;



}

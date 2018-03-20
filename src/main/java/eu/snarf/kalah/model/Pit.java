package eu.snarf.kalah.model;

import lombok.*;

import static javax.persistence.EnumType.STRING;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Frans on 3/19/2018.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)

@ToString

@Getter
@Entity
public class Pit extends BaseEntity implements Serializable {

    @Enumerated(STRING)
    private PitType pitType;

    @NotNull
    private int pitOrder;
    @NotNull
    private int seeds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="kalah_id", nullable=false)
    private Kalah kalah;

    @Enumerated(STRING)
    private Side side;

    @Builder
    private Pit(final int pitOrder, final int seeds, final PitType pitType, final Side side, final Kalah kalah){
        this.pitOrder = pitOrder;
        this.seeds = seeds;
        this.pitType = pitType;
        this.side = side;
        this.kalah = kalah;
    }

    public int countSeeds(){
        return this.seeds;
    }

    public void addSeed(final int extraSeeds){
     this.seeds = this.seeds + extraSeeds;
    }

    public int getPitOrder(){
        return pitOrder;
    }

    public boolean isFromActivePlayer(final Side side){
        return side == this.side;
    }

    public boolean isStore(){
        return PitType.STORE == this.pitType;
    }

    public void emptyPit(){
        this.seeds = 0;
    }
}

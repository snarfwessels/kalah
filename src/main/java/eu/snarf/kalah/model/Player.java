package eu.snarf.kalah.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author Frans on 3/19/2018.
 */
@Getter
@NoArgsConstructor

@Entity
public class Player extends BaseEntity implements Serializable {

    @Column(unique = true)
    private String name;

    @Builder
    private Player(final String name) {
        this.name = name;
    }

}

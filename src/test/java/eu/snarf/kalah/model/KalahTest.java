package eu.snarf.kalah.model;

import eu.snarf.kalah.TestData;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

/**
 * @author Frans on 3/20/2018.
 */
public class KalahTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    private Kalah fixture;

    @Before
    public void setUp() throws Exception {
        fixture = TestData.createInitialKalah();
    }

    @Test
    public void getPits() {
        final List<Pit> pits = fixture.getPits();
        softly.assertThat(pits.size()).isEqualTo(14);
        softly.assertThat(pits.get(0).getPitOrder()).isEqualTo(1);
        softly.assertThat(pits.get(0).getSide()).isEqualTo(Side.SOUTH);
        softly.assertThat(pits.get(13).getPitOrder()).isEqualTo(14);
        softly.assertThat(pits.get(13).getSide()).isEqualTo(Side.NORTH);
    }

    @Test
    public void getOwnStore() {
        final Pit storNorth = fixture.getOwnStore(Side.NORTH);
        softly.assertThat(storNorth.isStore()).isTrue();
        softly.assertThat(storNorth.getSide()).isEqualTo(Side.NORTH);
    }

    @Test
    public void getPitForOrder() {
        final Pit pitFive = fixture.getPitForOrder(5);
        softly.assertThat(pitFive.getPitOrder()).isEqualTo(5);
    }

    @Test
    public void isGameFinishedNotFinished() {
        boolean result = fixture.isGameFinished();
        softly.assertThat(result).isFalse();
    }

    @Test
    public void isGameFinishedFinished() {
        emptySideSouth(fixture);

        boolean result = fixture.isGameFinished();

        softly.assertThat(result).isTrue();
    }

    @Test
    public void determineWinner() {
        emptySideSouth(fixture);
        fixture.getPitForOrder(7).addSeed(100);

        fixture.determineWinner();

        softly.assertThat(fixture.getMessages().getInfoMessages().get(0)).isEqualTo("And the winnar is "+Side.SOUTH);
    }

    private void emptySideSouth(final Kalah kalah){
        kalah.getPitForOrder(1).emptyPit();
        kalah.getPitForOrder(2).emptyPit();
        kalah.getPitForOrder(3).emptyPit();
        kalah.getPitForOrder(4).emptyPit();
        kalah.getPitForOrder(5).emptyPit();
        kalah.getPitForOrder(6).emptyPit();
    }
}
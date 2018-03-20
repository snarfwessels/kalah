package eu.snarf.kalah.services;

import eu.snarf.kalah.exceptions.Messages;
import eu.snarf.kalah.model.Kalah;
import eu.snarf.kalah.model.Pit;
import eu.snarf.kalah.model.Side;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * @author Frans on 3/20/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class KalahValidationServiceTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    private KalahValidationService fixture;

    @Mock
    private Kalah kalah;
    @Mock
    private Pit pit;

    private Messages messages;

    @Before
    public void setUp() throws Exception {
        fixture = new KalahValidationService();
        messages = new Messages();
        when(pit.getSide()).thenReturn(Side.NORTH);
        when(kalah.getMessages()).thenReturn(messages);
    }

    @Test
    public void isItYourTurnYes() {
        when(kalah.getActiveSide()).thenReturn(Side.NORTH);

        fixture.isItYourTurn(kalah, pit);

        softly.assertThat(messages.hasErrors()).isFalse();
    }

    @Test
    public void isItYourTurnNo() {
        when(kalah.getActiveSide()).thenReturn(Side.SOUTH);

        fixture.isItYourTurn(kalah, pit);

        softly.assertThat(messages.hasErrors()).isTrue();
    }

    @Test
    public void isValidSowingPitTrue() {
        when(pit.getSeeds()).thenReturn(6);
        when(pit.isStore()).thenReturn(false);

        fixture.isValidSowingPit(pit, messages);

        softly.assertThat(messages.hasErrors()).isFalse();
    }

    @Test
    public void isValidSowingPitFalse() {
        when(pit.getSeeds()).thenReturn(0);
        when(pit.isStore()).thenReturn(true);

        fixture.isValidSowingPit(pit, messages);

        softly.assertThat(messages.hasErrors()).isTrue();
    }
}
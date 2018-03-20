package eu.snarf.kalah.services;

import eu.snarf.kalah.model.Player;
import eu.snarf.kalah.repositories.PlayerRepo;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Frans on 3/20/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {

    private PlayerService fixture;

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Mock
    private PlayerRepo playerRepo;

    @Before
    public void setUp() throws Exception {
        fixture = new PlayerService(playerRepo);
    }

    @Test
    public void getPlayer() {
        fixture.getPlayer("My Name");

        verify(playerRepo).findByName("My Name");
    }

    @Test
    public void createPlayer() {
        fixture.createPlayer("My Name");

        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepo, times(1)).save(captor.capture());
        Player player = captor.getValue();

        softly.assertThat(player.getName()).isEqualTo("My Name");
    }
}
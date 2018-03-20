package eu.snarf.kalah.services;

import eu.snarf.kalah.TestData;
import eu.snarf.kalah.exceptions.KalahException;
import eu.snarf.kalah.exceptions.Messages;
import eu.snarf.kalah.model.*;
import eu.snarf.kalah.model.dto.PlayerDto;
import eu.snarf.kalah.model.dto.StartAction;
import eu.snarf.kalah.repositories.KalahRepo;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Frans on 3/20/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class KalahServiceTest {

    private KalahService fixture;

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Mock
    private KalahRepo kalahRepo;
    @Mock
    private PlayerService playerService;
    @Mock
    private KalahValidationService kalahValidationService;

    private StartAction startAction;

    @Mock
    private Player player1, player2;

    @Mock
    private Kalah kalah;

    @Before
    public void setUp(){
        fixture = new KalahService(kalahRepo, playerService, kalahValidationService);
        startAction = StartAction.builder()
                .playerOne(PlayerDto.builder().name("Jantje").side(Side.NORTH).build())
                .playerTwo(PlayerDto.builder().name("Pietje").side(Side.SOUTH).build())
                .build();
    }


    @Test
    public void startNewGameWithExistingPlayers() {
        when(playerService.getPlayer(anyString())).thenReturn(player1).thenReturn(player2);

        fixture.startNewGame(startAction);

        ArgumentCaptor<Kalah> captor = ArgumentCaptor.forClass(Kalah.class);

        verify(kalahRepo, times(1)).save(captor.capture());
        Kalah kalah = captor.getValue();
        softly.assertThat(kalah).isNotNull();

        verify(playerService, times(2)).getPlayer(anyString());

        checkStartGameKalah(kalah);
    }

    @Test
    public void startNewGameWithoutExistingPlayers() {
        when(playerService.getPlayer(anyString())).thenReturn(null);
        when(playerService.createPlayer(anyString())).thenReturn(player1).thenReturn(player2);

        fixture.startNewGame(startAction);

        verify(playerService, times(2)).createPlayer(anyString());

        ArgumentCaptor<Kalah> captor = ArgumentCaptor.forClass(Kalah.class);
        verify(kalahRepo, times(1)).save(captor.capture());
        Kalah kalah = captor.getValue();

        checkStartGameKalah(kalah);
    }

    @Test
    public void getGameCorrectly() {
        when(kalahRepo.findById(anyLong())).thenReturn(Optional.of((kalah)));

        final Kalah result = fixture.getGame(1L);

        softly.assertThat(result).isNotNull();
    }

    @Test(expected = KalahException.class)
    public void getGameNotexisting() {
        when(kalahRepo.findById(anyLong())).thenReturn(Optional.empty());
        fixture.getGame(1L);
    }

    @Test
    public void getGames() {
        when(kalahRepo.findAll()).thenReturn(Collections.singletonList(kalah));

        final List<Kalah> results = fixture.getGames();

        softly.assertThat(results.size()).isEqualTo(1);
    }

    @Test
    public void sow() {
        Kalah kalah = TestData.createInitialKalah();

        when(kalahRepo.findById(1L)).thenReturn(Optional.of(kalah));

        fixture.sow(1, 13);

        ArgumentCaptor<Kalah> captor = ArgumentCaptor.forClass(Kalah.class);
        verify(kalahRepo, times(1)).save(captor.capture());
        Kalah result = captor.getValue();

        softly.assertThat(result).isNotNull();

        checkSowGameKalah(result, 13);
    }

    private void checkStartGameKalah(final Kalah kalah){
        softly.assertThat(kalah).isNotNull();
        softly.assertThat(kalah.getActiveSide()).isEqualTo(Side.NORTH);
        softly.assertThat(kalah.isEnded()).isFalse();
        softly.assertThat(kalah.getPits().size()).isEqualTo(14);
        int northPits = 0;
        int southPits = 0;
        for(Pit pit: kalah.getPits()){
            if(pit.isStore()){
                softly.assertThat(pit.countSeeds()).isEqualTo(0);
            } else {
                softly.assertThat(pit.countSeeds()).isEqualTo(6);
            }
            if(Side.NORTH ==pit.getSide()){
                northPits++;
            } else {
                southPits++;
            }
        }
        softly.assertThat(northPits).isEqualTo(southPits);
        softly.assertThat(kalah.getPlayers().size()).isEqualTo(2);
    }

    private void checkSowGameKalah(final Kalah kalah, final int avtivePit){
        softly.assertThat(kalah).isNotNull();
        softly.assertThat(kalah.getActiveSide()).isEqualTo(Side.SOUTH);
        softly.assertThat(kalah.isEnded()).isFalse();
        softly.assertThat(kalah.getPits().size()).isEqualTo(14);
        int northPits = 0;
        int southPits = 0;
        for(Pit pit: kalah.getPits()){
            if(pit.isStore()){
                if(pit.isFromActivePlayer(Side.NORTH)){
                    softly.assertThat(pit.countSeeds()).isEqualTo(1);
                } else {
                    softly.assertThat(pit.countSeeds()).isEqualTo(0);
                }
            } else if(pit.getPitOrder() == avtivePit){
                softly.assertThat(pit.countSeeds()).isEqualTo(0);
            } else if(pit.getPitOrder()>avtivePit || pit.getPitOrder() < 6){
                softly.assertThat(pit.countSeeds()).isEqualTo(7);
            } else {
                softly.assertThat(pit.countSeeds()).isEqualTo(6);
            }
            if(Side.NORTH ==pit.getSide()){
                northPits++;
            } else {
                southPits++;
            }
        }
        softly.assertThat(northPits).isEqualTo(southPits);
        softly.assertThat(kalah.getPlayers().size()).isEqualTo(2);
    }
}
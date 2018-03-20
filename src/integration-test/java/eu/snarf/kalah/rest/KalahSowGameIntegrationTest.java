package eu.snarf.kalah.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.snarf.kalah.model.Player;
import eu.snarf.kalah.model.Side;
import eu.snarf.kalah.model.dto.KalahDto;
import eu.snarf.kalah.model.dto.PlayerDto;
import eu.snarf.kalah.model.dto.StartAction;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static io.restassured.RestAssured.given;

/**
 * @author Frans on 3/20/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class KalahSowGameIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    public void testSowGame() throws IOException {
        final KalahDto dto = initGame();

        String action = "{\"gameId\": "+dto.getId()+", \"fromPit\": 8}";

        Response response = sowAction(action);

        softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);

        KalahDto result = objectMapper.readValue(response.getBody().prettyPrint(),
                new TypeReference<KalahDto>() {});

        softly.assertThat(result.isEnded()).isFalse();
        // Started in 8 with 6 stones, so ended in 14, NORTH Store
        // There is another turn for NORTH
        softly.assertThat(result.getActiveSide()).isEqualTo(Side.NORTH);
        //No winner
        softly.assertThat(result.getMessages().hasInfo()).isFalse();
        softly.assertThat(result).isNotNull();
    }



    private Response sowAction(final String sowAction) {
        return given()
                .contentType("application/json").
                        body(sowAction).
                        when().
                        put("/kalah");
    }

    private Response startGame(final String kalah) {
        return given()
                .contentType("application/json").
                        body(kalah).
                        when().
                        post("/kalah");
    }

    private KalahDto initGame() throws IOException {
        final Player jantje = Player.builder().name("Jantje").build();
        final Player pietje = Player.builder().name("Pietje").build();
        playerRepo.save(jantje);
        playerRepo.save(pietje);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        StartAction startAction = StartAction.builder()
                .playerOne(PlayerDto.builder().name("Jantje").side(Side.NORTH).build())
                .playerTwo(PlayerDto.builder().name("Pietje").side(Side.SOUTH).build())
                .build();

        String kalahJson = objectMapper.writeValueAsString(startAction);
        Response response = startGame(kalahJson);
        return objectMapper.readValue(response.getBody().prettyPrint(),
                new TypeReference<KalahDto>() {});
    }
}

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
 * this test makes sure that the initial setup is correct
 *
 *
 * @author Frans on 3/20/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class KalahStartGameIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    public void testCreateGame() throws IOException {
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

        Response response = performStartKalah(kalahJson);

        softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);

        KalahDto kalahDtoResult =  objectMapper.readValue(response.getBody().prettyPrint(),
                new TypeReference<KalahDto>() {});

        softly.assertThat(kalahDtoResult).isNotNull();
    }

    private Response performStartKalah(final String kalah) {
        return given()
                .contentType("application/json").
                        body(kalah).
                        when().
                        post("/kalah");
    }

}

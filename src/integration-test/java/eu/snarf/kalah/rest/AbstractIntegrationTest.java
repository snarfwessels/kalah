package eu.snarf.kalah.rest;

import eu.snarf.kalah.repositories.KalahPlayerRepo;
import eu.snarf.kalah.repositories.KalahRepo;
import eu.snarf.kalah.repositories.PitRepo;
import eu.snarf.kalah.repositories.PlayerRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Frans on 3/20/2018.
 */
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("it")
@SpringBootTest
@DirtiesContext
public abstract class AbstractIntegrationTest implements IntegrationTest {

    @Autowired
    KalahRepo kalahRepo;
    @Autowired
    PlayerRepo playerRepo;
    @Autowired
    PitRepo pitRepo;
    @Autowired
    KalahPlayerRepo kalahPlayerRepo;


    @Before
    public void init() {
        tearDown();
        initTest();
    }

    @After
    public void tearDown() {
        kalahPlayerRepo.deleteAllInBatch();
        playerRepo.deleteAllInBatch();
        pitRepo.deleteAllInBatch();
        kalahRepo.deleteAllInBatch();
    }

    /**
     *  Default init implementation. Override if you need to.
     */
    public void initTest() {
        // Default implementation
    }


}

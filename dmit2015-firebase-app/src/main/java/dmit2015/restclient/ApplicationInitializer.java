package dmit2015.restclient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import net.datafaker.Faker;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ApplicationInitializer {
    private final Logger _logger = Logger.getLogger(ApplicationInitializer.class.getName());

    @Inject
    @RestClient
    private VideoGameMpRestClient _gameRestClient;

    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object event) {
        _logger.info("Preloading data");

        try {
            // Create a data Faker instance
            var faker = new Faker();
            // Create data for 20 different video games if there are none in the Firebase Realtime DB
            var allVideoGamesMap = _gameRestClient.findAll();
            if (allVideoGamesMap.isEmpty()) {
                for (int count = 1; count <= 20; count++) {
                    // Create a VideoGame instance
                    var currentGame = new VideoGame();
                    // Generate and assign data for Genre, Platform, and Title
                    currentGame.setGenre(faker.videoGame().genre());
                    currentGame.setPlatform(faker.videoGame().platform());
                    currentGame.setTitle(faker.videoGame().title());
                    // Post the VideoGame data to the REST API endpoint
                    _gameRestClient.create(currentGame);
                }
            }


        } catch (Exception ex) {
            _logger.log(Level.FINE, ex.getMessage(), ex);
        }

    }
}
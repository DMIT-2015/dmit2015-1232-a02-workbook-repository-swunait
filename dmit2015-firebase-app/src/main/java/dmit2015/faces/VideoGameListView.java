package dmit2015.faces;

import dmit2015.restclient.VideoGame;
import dmit2015.restclient.VideoGameMpRestClient;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;


@Named("currentVideoGameListView")
@ViewScoped
public class VideoGameListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private VideoGameMpRestClient _videogameMpRestClient;

    @Getter
    private Map<String, VideoGame> videogameMap;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            videogameMap = _videogameMpRestClient.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}
package dmit2015.faces;

import dmit2015.restclient.VideoGame;
import dmit2015.restclient.VideoGameMpRestClient;

import jakarta.json.JsonObject;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("currentVideoGameCreateView")
@RequestScoped
public class VideoGameCreateView {

    @Inject
    @RestClient
    private VideoGameMpRestClient _videogameMpRestClient;

    @Getter
    private VideoGame newVideoGame = new VideoGame();

    public String onCreateNew() {
        String nextPage = null;
        try {
            JsonObject responseBody = _videogameMpRestClient.create(newVideoGame);
            String documentKey = responseBody.getString("name");
            newVideoGame = new VideoGame();
            Messages.addFlashGlobalInfo("Create was successful. {0}", documentKey);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}
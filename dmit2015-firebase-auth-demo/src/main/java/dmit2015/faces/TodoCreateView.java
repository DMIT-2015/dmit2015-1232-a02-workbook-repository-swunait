package dmit2015.faces;

import dmit2015.restclient.Todo;
import dmit2015.restclient.TodoAuthMpRestClient;

import jakarta.json.JsonObject;
import lombok.Getter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalDateTime;

@Named("currentTodoCreateView")
@RequestScoped
public class TodoCreateView {

    @Inject
    @RestClient
    private TodoAuthMpRestClient _todoMpRestClient;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Getter
    private Todo newTodo = new Todo();

    public String onCreateNew() {
        String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
        String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
        String nextPage = null;
        try {
            newTodo.setCreated(LocalDateTime.now());
            JsonObject responseBody = _todoMpRestClient.create(userUID, newTodo, token);
            String documentKey = responseBody.getString("name");
            newTodo = new Todo();
            Messages.addFlashGlobalInfo("Create was successful. {0}", documentKey);
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}
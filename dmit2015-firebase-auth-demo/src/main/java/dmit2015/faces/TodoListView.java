package dmit2015.faces;

import dmit2015.restclient.Todo;
import dmit2015.restclient.TodoAuthMpRestClient;
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


@Named("currentTodoListView")
@ViewScoped
public class TodoListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    @RestClient
    private TodoAuthMpRestClient _todoMpRestClient;

    @Inject
    private FirebaseLoginSession _firebaseLoginSession;

    @Getter
    private Map<String, Todo> todoMap;

    @PostConstruct  // After @Inject is complete
    public void init() {
        String token = _firebaseLoginSession.getFirebaseUser().getIdToken();
        String userUID = _firebaseLoginSession.getFirebaseUser().getLocalId();
        try {
            todoMap = _todoMpRestClient.findAll(userUID, token);
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}
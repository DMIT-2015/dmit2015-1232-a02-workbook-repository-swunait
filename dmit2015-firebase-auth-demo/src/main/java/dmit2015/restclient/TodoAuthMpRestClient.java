package dmit2015.restclient;

import jakarta.enterprise.context.RequestScoped;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.LinkedHashMap;

/**
 * The baseUri for the web AuthMpRestClient be set in either microprofile-config.properties (recommended)
 * or in this file using @RegisterRestClient(baseUri = "http://server/path").
 * <p>
 * To set the baseUri in microprofile-config.properties:
 * 1) Open src/main/resources/META-INF/microprofile-config.properties
 * 2) Add a key/value pair in the following format:
 * package-name.ClassName/mp-rest/url=baseUri
 * For example:
 * package-name:    dmit2015.restclient
 * ClassName:       TodoAuthMpRestClient
 * baseUri:         http://localhost:8080/contextName
 * The key/value pair you need to add is:
 * <code>
 * dmit2015.restclient.TodoAuthMpRestClient/mp-rest/url=http://localhost:8080/contextName
 * </code>
 * <p>
 * To use the client interface from an environment does support CDI, add @Inject and @RestClient before the field declaration such as:
 * <code>
 *
 * @Inject
 * @RestClient private TodoAuthMpRestClient _todoAuthMpRestClient;
 * </code>
 * <p>
 * To use the client interface from an environment that does not support CDI, you can use the RestClientBuilder class to programmatically build an instance as follows:
 * <code>
 * URI apiURI = new URI("http://sever/contextName");
 * TodoAuthMpRestClient _todoAuthMpRestClient = RestClientBuilder.newBuilder().baseUri(apiURi).build(TodoAuthMpRestClient.class);
 * </code>
 * To filter data with firebase you need to add an indexOf security rule
 * <code>
 * {
 * "rules": {
 * "TodoItem": {
 * "$uid": {
 * // Allow only authenticated content owners access to their data
 * ".read": "auth !== null && auth.uid === $uid",
 * ".write": "auth !== null && auth.uid === $uid",
 * ".indexOn": ["done"]
 * }
 * }
 * }
 * }
 * </code>
 */
@RequestScoped
@RegisterRestClient(baseUri = "https://dmit2015-1232-swu02-default-rtdb.firebaseio.com")
public interface TodoAuthMpRestClient {

    String DOCUMENT_URL = "/Todo/{uid}";

    @POST
    @Path(DOCUMENT_URL + ".json")
    JsonObject create(@PathParam("uid") String userId, Todo newTodo, @QueryParam("auth") String token);

    @GET
    @Path(DOCUMENT_URL + ".json")
    LinkedHashMap<String, Todo> findAll(@PathParam("uid") String userId, @QueryParam("auth") String token);

    @GET
    @Path(DOCUMENT_URL + "/{key}.json")
    Todo findById(@PathParam("uid") String userId, @PathParam("key") String key, @QueryParam("auth") String token);

    @PUT
    @Path(DOCUMENT_URL + "/{key}.json")
    Todo update(@PathParam("uid") String userId, @PathParam("key") String key, Todo updatedTodo, @QueryParam("auth") String token);

    @DELETE
    @Path(DOCUMENT_URL + "/{key}.json")
    void delete(@PathParam("uid") String userId, @PathParam("key") String key, @QueryParam("auth") String token);

}
package dmit2015.resource;

import common.validator.BeanValidator;
import dmit2015.entity.TodoItem;
import dmit2015.repository.TodoItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;


/**
 * * Web API with CRUD methods for managing TodoItem.
 *
 *  URI						    Http Method     Request Body		                        Description
 * 	----------------------      -----------		------------------------------------------- ------------------------------------------
 *	/restapi/TodoItems			POST			{                                           Create a new TodoItem
 *                                              "name":"Demo DMIT2015 assignment 1",
 *                                         	    "complete":false
 *                                         	    }
 * 	/restapi/TodoItems/{id}		GET			                                                Find one TodoItem with a id value
 * 	/restapi/TodoItems		    GET			                                                Find all TodoItem
 * 	/restapi/TodoItems/{id}     PUT             {                                           Update the TodoItem
 * 	                                            "id":5,
 * 	                                            "name":"Submitted DMIT2015 assignment 7",
 *                                              "complete":true
 *                                              }
 * 	/restapi/TodoItems/{id}		DELETE			                                            Remove the TodoItem
 *
 */

@ApplicationScoped
// This is a CDI-managed bean that is created only once during the life cycle of the application
@Path("TodoItems")	        // All methods of this class are associated this URL path
@Consumes(MediaType.APPLICATION_JSON)	// All methods this class accept only JSON format data
@Produces(MediaType.APPLICATION_JSON)	// All methods returns data that has been converted to JSON format
public class TodoItemResource {

//    @Context
    @Inject
    private UriInfo uriInfo;

    @Inject
    private TodoItemRepository todoItemRepository;

    @POST   // POST: /restapi/TodoItems
    public Response postTodoItem(TodoItem newTodoItem) {
        if (newTodoItem == null) {
            throw new BadRequestException();
        }

        String errorMessage = BeanValidator.validateBean(newTodoItem);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        todoItemRepository.add(newTodoItem);
        URI todoItemsUri = uriInfo.getAbsolutePathBuilder().path(newTodoItem.getId().toString()).build();
        return Response.created(todoItemsUri).build();
    }

    @GET    // GET: /restapi/TodoItems/5
    @Path("{id}")
    public Response getTodoItem(@PathParam("id") Long id) {
        Optional<TodoItem> optionalTodoItem = todoItemRepository.findById(id);

        if (optionalTodoItem.isEmpty()) {
            throw new NotFoundException();
        }
        TodoItem existingTodoItem = optionalTodoItem.get();

        return Response.ok(existingTodoItem).build();
    }

    @GET    // GET: /restapi/TodoItems
    public Response getTodoItems() {
        return Response.ok(todoItemRepository.findAll()).build();
    }

    @PUT    // PUT: /restapi/TodoItems/5
    @Path("{id}")
    public Response updateTodoItem(@PathParam("id") Long id, TodoItem updatedTodoItem) {
        if (!id.equals(updatedTodoItem.getId())) {
            throw new BadRequestException();
        }

        String errorMessage = BeanValidator.validateBean(updatedTodoItem);
        if (errorMessage != null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errorMessage)
                    .build();
        }

        TodoItem existingTodoItem = todoItemRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);

        // Copy data from the updated entity to the existing entity
        existingTodoItem.setVersion(updatedTodoItem.getVersion());
        existingTodoItem.setTask(updatedTodoItem.getTask());
        existingTodoItem.setDone(updatedTodoItem.isDone());

        try {
            todoItemRepository.update(existingTodoItem);
        } catch (OptimisticLockException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("You are updating an old version of the data. Please fetch new version.")
                    .build();
        } catch (Exception ex) {
            return Response
                    .serverError()
                    .entity(ex.getMessage())
                    .build();
        }

        return Response.ok(existingTodoItem).build();
    }

    @DELETE // DELETE: /restapi/TodoItems/5
    @Path("{id}")
    public Response deleteTodoItem(@PathParam("id") Long id) {

        TodoItem existingTodoItem = todoItemRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        todoItemRepository.deleteById(id);

        return Response.noContent().build();
    }

}
package dmit2015.restclient;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Todo {

    @NotBlank(message = "Task is required")
    private String task;

    private boolean done;

    private LocalDateTime created;

}

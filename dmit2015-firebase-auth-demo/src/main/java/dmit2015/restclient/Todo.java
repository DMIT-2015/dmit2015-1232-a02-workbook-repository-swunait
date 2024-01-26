package dmit2015.restclient;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Todo {

    private String task;
    private boolean done;
    private LocalDateTime created;

}

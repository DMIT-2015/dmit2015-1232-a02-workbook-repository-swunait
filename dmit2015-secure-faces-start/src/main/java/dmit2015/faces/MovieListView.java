package dmit2015.faces;

import dmit2015.entity.Movie;
import dmit2015.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import lombok.Getter;
import org.omnifaces.util.Messages;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named("currentMovieListView")
@ViewScoped
public class MovieListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** The SecurityContext can be used to get the authenticated username and if a user in a specific role */
    @Inject
    private SecurityContext _securityContext;

    @Inject
    private MovieRepository _movieRepository;

    @Getter
    private List<Movie> movieList;

    @Getter
    private String subject;

//    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            movieList = _movieRepository.findAll();
        } catch (RuntimeException ex) {
            Messages.addGlobalWarn(ex.getMessage());
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}
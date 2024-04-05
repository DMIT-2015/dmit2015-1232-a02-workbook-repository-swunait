package dmit2015.faces;

import dmit2015.entity.Movie;
import dmit2015.repository.MovieRepository;

import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("currentMovieCreateView")
@RequestScoped
public class MovieCreateView {

    @Inject
    private MovieRepository _movieRepository;

    @Getter
    private Movie newMovie = new Movie();

    public String onCreateNew() {
        String nextPage = "";
        try {
            _movieRepository.add(newMovie);
            Messages.addFlashGlobalInfo("Create was successful. {0}", newMovie.getId());
            nextPage = "index?faces-redirect=true";
        } catch (RuntimeException ex) {
            Messages.addGlobalWarn(ex.getMessage());
        } catch (Exception e) {
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}
package dmit2015.restclient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class VideoGame {

    @NotBlank(message = "Platform cannot be blank")
    private String platform;
    @NotBlank(message = "Title cannot be blank")
    @Size(min=3, max = 20, message = "Title must contain 3 to 20 characters.")
    private String title;

    @NotEmpty(message = "Genre cannot be empty")
    private String genre;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

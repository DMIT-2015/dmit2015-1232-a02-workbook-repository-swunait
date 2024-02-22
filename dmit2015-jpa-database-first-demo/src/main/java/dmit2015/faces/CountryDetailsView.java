package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.persistence.CountryRepository;

import lombok.Getter;
import lombok.Setter;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.omnifaces.util.Faces;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Named("currentCountryDetailsView")
@ViewScoped
public class CountryDetailsView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private CountryRepository _countryRepository;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private String editId;

    @Getter
    private Country existingCountry;

    @PostConstruct
    public void init() {
        Optional<Country> optionalCountry = _countryRepository.findById(editId);
        if (optionalCountry.isPresent()) {
            existingCountry = optionalCountry.orElseThrow();
        } else {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }
}
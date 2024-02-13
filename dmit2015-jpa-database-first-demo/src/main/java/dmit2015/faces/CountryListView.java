package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.persistence.CountryRepository;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named("currentCountryListView")
@ViewScoped
public class CountryListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private CountryRepository _countryRepository;

    @Getter
    private List<Country> countryList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            countryList = _countryRepository.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}
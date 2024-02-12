package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.persistence.CountryRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("currentCountryCreateView")
@RequestScoped
public class CountryCreateView {

    @Inject
    private CountryRepository _countryRepository;

    @Getter
    private Country newCountry = new Country();

    @PostConstruct  // After @Inject is complete
    public void init() {

    }

    public String onCreateNew() {
        String nextPage = "";
        try {
            _countryRepository.add(newCountry);
            Messages.addFlashGlobalInfo("Create was successful. {0}", newCountry.getCountryId());
            nextPage = "index?faces-redirect=true";
        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}
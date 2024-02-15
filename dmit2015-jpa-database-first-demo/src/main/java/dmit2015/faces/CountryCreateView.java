package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.entity.Region;
import dmit2015.persistence.CountryRepository;

import dmit2015.persistence.RegionRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.math.BigInteger;
import java.util.List;

@Named("currentCountryCreateView")
@RequestScoped
public class CountryCreateView {

    @Inject
    private CountryRepository _countryRepository;

    @Getter
    private Country newCountry = new Country();

    @Inject
    private RegionRepository _regionRepository;

    @Getter
    private List<Region> regions;

    @Getter @Setter
    private BigInteger selectedRegionId;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            regions = _regionRepository.findAll();
        } catch (RuntimeException ex) {
            Messages.addGlobalError("Error fetching regions {0}", ex.getMessage());
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public String onCreateNew() {
        String nextPage = "";
        try {
            // Set the region for the country
            if (selectedRegionId != null) {
                var selectedOptionalRegion = _regionRepository.findById(selectedRegionId);
                if (selectedOptionalRegion.isPresent()) {
                    var selectedRegion = selectedOptionalRegion.orElseThrow();
                    newCountry.setRegionsByRegionId(selectedRegion);
                }
            } else {
                newCountry.setRegionsByRegionId(null);
            }

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
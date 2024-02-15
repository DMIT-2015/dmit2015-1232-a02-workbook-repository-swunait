package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.entity.Region;
import dmit2015.persistence.CountryRepository;

import dmit2015.persistence.RegionRepository;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Named("currentCountryEditView")
@ViewScoped
public class CountryEditView implements Serializable {
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

    @Inject
    private RegionRepository _regionRepository;

    @Getter
    private List<Region> regions;

    @Getter @Setter
    private BigInteger selectedRegionId;

    @PostConstruct
    public void init() {
        regions = _regionRepository.findAll();

        if (!Faces.isPostback()) {
            if (editId != null) {
                Optional<Country> optionalCountry = _countryRepository.findById(editId);
                if (optionalCountry.isPresent()) {
                    existingCountry = optionalCountry.orElseThrow();
                    // Set the selectedRegionId
                    selectedRegionId = existingCountry.getRegionsByRegionId().getRegionId();

                } else {
                    Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
                }
            } else {
                Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
            }
        }
    }

    public String onUpdate() {
        String nextPage = "";
        try {
            // Set the region for the country
            if (selectedRegionId != null) {
                var selectedOptionalRegion = _regionRepository.findById(selectedRegionId);
                if (selectedOptionalRegion.isPresent()) {
                    var selectedRegion = selectedOptionalRegion.orElseThrow();
                    existingCountry.setRegionsByRegionId(selectedRegion);
                }
            } else {
                existingCountry.setRegionsByRegionId(null);
            }

            _countryRepository.update(editId, existingCountry);
            Messages.addFlashGlobalInfo("Update was successful.");
            nextPage = "index?faces-redirect=true";
        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Update was not successful.");
        }
        return nextPage;
    }
}
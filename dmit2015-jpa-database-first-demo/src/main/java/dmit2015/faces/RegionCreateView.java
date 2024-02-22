package dmit2015.faces;

import dmit2015.entity.Region;
import dmit2015.persistence.RegionRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("currentRegionCreateView")
@RequestScoped
public class RegionCreateView {

    @Inject
    private RegionRepository _regionRepository;

    @Getter
    private Region newRegion = new Region();

    @PostConstruct  // After @Inject is complete
    public void init() {

    }

    public String onCreateNew() {
        String nextPage = "";
        try {
            _regionRepository.add(newRegion);
            Messages.addFlashGlobalInfo("Create was successful. {0}", newRegion.getRegionId());
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
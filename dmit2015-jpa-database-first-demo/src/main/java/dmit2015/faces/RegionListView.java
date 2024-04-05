package dmit2015.faces;

import dmit2015.entity.Region;
import dmit2015.persistence.RegionRepository;
import lombok.Getter;
import org.omnifaces.util.Messages;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Named("currentRegionListView")
@ViewScoped
public class RegionListView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private RegionRepository _regionRepository;

    @Getter
    private List<Region> regionList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            regionList = _regionRepository.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }
}
package dmit2015.faces;

import dmit2015.entity.Region;
import dmit2015.persistence.RegionRepository;

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
import java.math.BigInteger;
import java.util.Optional;

@Named("currentRegionDetailsView")
@ViewScoped
public class RegionDetailsView implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private RegionRepository _regionRepository;

    @Inject
    @ManagedProperty("#{param.editId}")
    @Getter
    @Setter
    private BigInteger editId;

    @Getter
    private Region existingRegion;

    @PostConstruct
    public void init() {
        Optional<Region> optionalRegion = _regionRepository.findById(editId);
        if (optionalRegion.isPresent()) {
            existingRegion = optionalRegion.orElseThrow();
        } else {
            Faces.redirect(Faces.getRequestURI().substring(0, Faces.getRequestURI().lastIndexOf("/")) + "/index.xhtml");
        }
    }
}
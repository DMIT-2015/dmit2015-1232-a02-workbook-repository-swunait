package dmit2015.entity;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "REGIONS", schema = "HR", catalog = "")
public class Region {

    @Id
    @Column(name = "REGION_ID", nullable = false, precision = 0)
    private BigInteger regionId;
    @Basic
    @Column(name = "REGION_NAME", nullable = true, length = 25)
    private String regionName;
    @OneToMany(mappedBy = "regionsByRegionId")
    private Collection<Country> countriesByRegionId;

    public BigInteger getRegionId() {
        return regionId;
    }

    public void setRegionId(BigInteger regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(regionId, region.regionId) && Objects.equals(regionName, region.regionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionId, regionName);
    }

    public Collection<Country> getCountriesByRegionId() {
        return countriesByRegionId;
    }

    public void setCountriesByRegionId(Collection<Country> countriesByRegionId) {
        this.countriesByRegionId = countriesByRegionId;
    }
}

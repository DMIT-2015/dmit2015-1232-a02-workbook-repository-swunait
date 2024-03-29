package dmit2015.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "COUNTRIES", schema = "HR")
public class Country {

    @Id
    @Column(name = "COUNTRY_ID", nullable = false, length = 2)
    @Pattern(regexp = "^[a-zA-Z]{2}$", message = "CountryId must contain exactly 2 characters")
    private String countryId;
    @Basic
    @Column(name = "COUNTRY_NAME", nullable = true, length = 60)
    private String countryName;
    @Basic
    @Column(name = "REGION_ID", nullable = true, precision = 0)
    private BigInteger regionId;
    @ManyToOne
    @JoinColumn(name = "REGION_ID", referencedColumnName = "REGION_ID", insertable=false, updatable=false)
    private Region regionsByRegionId;
    @OneToMany(mappedBy = "countriesByCountryId")
    private Collection<Location> locationsByCountryId;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public BigInteger getRegionId() {
        return regionId;
    }

    public void setRegionId(BigInteger regionId) {
        this.regionId = regionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(countryId, country.countryId) && Objects.equals(countryName, country.countryName) && Objects.equals(regionId, country.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId, countryName, regionId);
    }

    public Region getRegionsByRegionId() {
        return regionsByRegionId;
    }

    public void setRegionsByRegionId(Region regionsByRegionId) {
        this.regionsByRegionId = regionsByRegionId;
    }

    public Collection<Location> getLocationsByCountryId() {
        return locationsByCountryId;
    }

    public void setLocationsByCountryId(Collection<Location> locationsByCountryId) {
        this.locationsByCountryId = locationsByCountryId;
    }
}

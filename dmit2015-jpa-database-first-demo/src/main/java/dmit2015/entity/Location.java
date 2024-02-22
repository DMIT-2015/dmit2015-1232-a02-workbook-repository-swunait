package dmit2015.entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "LOCATIONS", schema = "HR")
public class Location {

    @Id
    @Column(name = "LOCATION_ID", nullable = false, precision = 0)
    private Short locationId;
    @Basic
    @Column(name = "STREET_ADDRESS", nullable = true, length = 40)
    private String streetAddress;
    @Basic
    @Column(name = "POSTAL_CODE", nullable = true, length = 12)
    private String postalCode;
    @Basic
    @Column(name = "CITY", nullable = false, length = 30)
    private String city;
    @Basic
    @Column(name = "STATE_PROVINCE", nullable = true, length = 25)
    private String stateProvince;
    @Basic
    @Column(name = "COUNTRY_ID", nullable = true, length = 2, insertable=false, updatable=false)
    private String countryId;
    @OneToMany(mappedBy = "locationsByLocationId")
    private Collection<Department> departmentsByLocationId;
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "COUNTRY_ID")
    private Country countriesByCountryId;

    public Short getLocationId() {
        return locationId;
    }

    public void setLocationId(Short locationId) {
        this.locationId = locationId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(locationId, location.locationId) && Objects.equals(streetAddress, location.streetAddress) && Objects.equals(postalCode, location.postalCode) && Objects.equals(city, location.city) && Objects.equals(stateProvince, location.stateProvince) && Objects.equals(countryId, location.countryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, streetAddress, postalCode, city, stateProvince, countryId);
    }

    public Collection<Department> getDepartmentsByLocationId() {
        return departmentsByLocationId;
    }

    public void setDepartmentsByLocationId(Collection<Department> departmentsByLocationId) {
        this.departmentsByLocationId = departmentsByLocationId;
    }

    public Country getCountriesByCountryId() {
        return countriesByCountryId;
    }

    public void setCountriesByCountryId(Country countriesByCountryId) {
        this.countriesByCountryId = countriesByCountryId;
    }
}

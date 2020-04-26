package com.may.compositepk.onetoone;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SharedPK implements Serializable {

    @Column(name = "ITEM_ID")
    private Long id;

    @Column(name = "COUNTRY")
    private String country;

    public SharedPK() {
    }

    public SharedPK(String country) {
        this.country = country;
    }

    public SharedPK(Long id, String country) {
        this.id = id;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedPK itemId = (SharedPK) o;
        return Objects.equals(id, itemId.id) &&
            Objects.equals(country, itemId.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

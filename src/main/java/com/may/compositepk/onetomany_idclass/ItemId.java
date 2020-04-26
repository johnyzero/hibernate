package com.may.compositepk.onetomany_idclass;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class ItemId implements Serializable {

    private Long itemId;

    private String country;

    public ItemId() {
    }

    public ItemId(String country) {
        this.country = country;
    }

    public ItemId(Long itemId, String country) {
        this.itemId = itemId;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(itemId, ((ItemId)o).itemId) &&
            Objects.equals(country, ((ItemId) o).country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, country);
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long id) {
        this.itemId = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

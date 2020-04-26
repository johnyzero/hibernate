package com.may.compositepk.onetomany;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ItemId implements Serializable {

    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "COUNTRY")
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
        ItemId itemId = (ItemId) o;
        return Objects.equals(itemId, itemId.itemId) &&
            Objects.equals(country, itemId.country);
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

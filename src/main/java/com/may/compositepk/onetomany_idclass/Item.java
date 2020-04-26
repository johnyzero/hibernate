package com.may.compositepk.onetomany_idclass;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;

@Entity
@IdClass(value = ItemId.class) // composite id (id + country) @IdClass works only with SEQUENCE/TABLE strategies or if entity is not written (read-only)
public class Item {

    @Id
    @Column(name = "ITEM_ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // https://hibernate.atlassian.net/browse/HHH-10957
    private Long itemId;

    @Id
    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "ITEM_NAME")
    private String name;

    @OneToMany(mappedBy = "item")
    public List<Bid> bids = new ArrayList<>();

    public Item() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}

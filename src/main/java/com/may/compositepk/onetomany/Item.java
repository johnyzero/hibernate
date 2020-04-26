package com.may.compositepk.onetomany;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Item {

    @EmbeddedId
    private ItemId id;

    @Column(name = "ITEM_NAME")
    private String name;

    @OneToMany(mappedBy = "item")
    public List<Bid> bids = new ArrayList<>();

    public Item() {
    }

    public ItemId getId() {
        return id;
    }

    public void setId(ItemId id) {
        this.id = id;
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

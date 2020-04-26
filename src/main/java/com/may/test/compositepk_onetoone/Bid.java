package com.may.test.compositepk_onetoone;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class Bid {

    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "BID_ITEM_ID"))
    @AttributeOverride(name = "country", column = @Column(name = "BID_COUNTRY"))
    private SharedPK id;

    @Column(name = "BID_NAME")
    private String name;

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "BID_ITEM_ID", referencedColumnName = "ITEM_ID"),
        @JoinColumn(name = "BID_COUNTRY", referencedColumnName = "COUNTRY")
    })
    @MapsId
    private Item item;

    public SharedPK getId() {
        return id;
    }

    public void setId(SharedPK id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

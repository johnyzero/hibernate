package com.may.test.compositepk_onetomany;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
public class Bid {

    @Id
    @Column(name = "BID_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BID_ITEM_ID", insertable = false, updatable = false)
    private Long bidItemId;

    @Column(name = "BID_COUNTRY", insertable = false, updatable = false)
    private String bidCountry;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "BID_ITEM_ID", referencedColumnName = "ITEM_ID"),
        @JoinColumn(name = "BID_COUNTRY", referencedColumnName = "COUNTRY")
    })
    private Item item;

    @Column(name = "BID_NAME")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getBidItemId() {
        return bidItemId;
    }

    public void setBidItemId(Long bidItemId) {
        this.bidItemId = bidItemId;
    }

    public String getBidCountry() {
        return bidCountry;
    }

    public void setBidCountry(String bidCountry) {
        this.bidCountry = bidCountry;
    }
}

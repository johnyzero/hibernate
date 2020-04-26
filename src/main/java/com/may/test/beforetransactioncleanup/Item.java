package com.may.test.beforetransactioncleanup;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    protected String name;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @OrderColumn(name = "BID_POSITION") // Defaults to BIDS_ORDER
    @LazyCollection(LazyCollectionOption.EXTRA)
    public List<Bid> bids = new ArrayList<>();

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
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
}
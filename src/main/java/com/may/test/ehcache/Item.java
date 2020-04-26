package com.may.test.ehcache;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
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
import org.hibernate.annotations.NamedQuery;

@Entity
@Cacheable
@NamedQuery(name = "item_named_query", query = "SELECT i FROM com.may.test.ehcache.Item i", cacheable = true, cacheRegion = "item_query_cache")
//JPA Query Cache -> @javax.persistence.NamedQuery(name = "item_query", query = "SELECT i FROM com.may.test.ehcache.Item i", hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true"), @QueryHint(name = "org.hibernate.cacheRegion", value = "item_query")})
//Hibernate Entity Cache -> @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.may.test.ehcache.Item")
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
package com.may.associations.onetomany.orphanremoval;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String name;

    @OneToMany(mappedBy = "item",
               cascade = CascadeType.PERSIST,
               orphanRemoval = true) // Includes CascadeType.REMOVE (orphanRemoval is false by default because other entities may contain references to Bid instance) если бы значение было тру, то Bid инстансы бы удалялисьи из других коллекций
    protected Set<Bid> bids = new HashSet<>();

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

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

}

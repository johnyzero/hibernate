package com.may.inheritance.mappedsuperclass;

import javax.persistence.MappedSuperclass;

// Can not be @MappedSuperclass when it's a target class in associations!
@MappedSuperclass
public class BillingDetails {

    protected String owner;

    public BillingDetails() {

    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

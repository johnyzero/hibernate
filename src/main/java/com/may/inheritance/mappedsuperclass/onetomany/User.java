package com.may.inheritance.mappedsuperclass.onetomany;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class User {

    @Id
    @GeneratedValue
    protected Long id;

    @NotNull
    protected String username;

    @ManyToOne(fetch = FetchType.LAZY)
    protected BillingDetails defaultBilling;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BillingDetails getDefaultBilling() {
        return defaultBilling;
    }

    public void setDefaultBilling(BillingDetails defaultBilling) {
        this.defaultBilling = defaultBilling;
    }

}

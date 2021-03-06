package com.may.compositepk.onetoone;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
// tried to make one of ids to be autogenerated
//@SQLInsert(sql = "insert into item (country, item_name, version) values (?, ?, ?)") // https://vladmihalcea.com/how-to-map-a-composite-identifier-using-an-automatically-generatedvalue-with-jpa-and-hibernate/?unapproved=70652&moderation-hash=2f66997bd5575bb4088873b927ea934e#comment-70652
public class Item {

    @EmbeddedId
    private SharedPK id;

    @Column(name = "ITEM_NAME")
    private String name;

    @OneToOne(mappedBy = "item")
    private Bid bid;

    @Version
    @Column(insertable = false)
    private Integer version;

    public Item() {
    }

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


    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }
}

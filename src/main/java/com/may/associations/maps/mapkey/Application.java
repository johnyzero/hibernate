package com.may.associations.maps.mapkey;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class Application {

    @PersistenceContext
    private EntityManager em;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    @Order(0)
    @Transactional
    public void execute(ApplicationReadyEvent event) {
        Item someItem = new Item("item1");
        em.persist(someItem);

        Bid someBid = new Bid(new BigDecimal("123.00"), someItem);
        em.persist(someBid);
        someItem.getBids().put(someBid.getId(), someBid); // Optional...

        Bid secondBid = new Bid(new BigDecimal("456.00"), someItem);
        em.persist(secondBid);
        someItem.getBids().put(secondBid.getId(), secondBid); // Optional...
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Item item1 = (Item) em.createQuery("from Item i where i.name = 'item1'").getResultList().get(0);
        assertThat(item1.getBids().size(), is(2));

        for (Map.Entry<Long, Bid> entry : item1.getBids().entrySet()) {
            // The key is the identifier of each Bid
            assertThat(entry.getKey(), is(entry.getValue().getId()));
        }
    }

}

package com.may.associations.onetomany.list;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.List;

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
        Item someItem = new Item("Some Item");
        em.persist(someItem);

        Bid someBid = new Bid(new BigDecimal("124.00"), someItem);
        someItem.getBids().add(someBid);
        someItem.getBids().add(someBid); // No persistent effect!
        em.persist(someBid);

        Bid secondBid = new Bid(new BigDecimal("456.00"), someItem);
        someItem.getBids().add(secondBid);
        em.persist(secondBid);

        assertThat(someItem.getBids().size(), is(2));
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Item item = em.find(Item.class, 1L);
        List<Bid> bids =  item.getBids();
        assertThat(bids.size(), is(2));
        assertThat(bids.get(0).getAmount().compareTo(new BigDecimal("124")), is(0));
        assertThat(bids.get(1).getAmount().compareTo(new BigDecimal("456")), is(0));
    }

}

package com.may.associations.onetomany.list;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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

        Bid bid1 = new Bid(new BigDecimal("124.00"), someItem);
        someItem.getBids().add(bid1);
        someItem.getBids().add(bid1); // No persistent effect! Be careful since next time after loading items you will have one null in Item.bids collection
        em.persist(bid1);

        Bid bid2 = new Bid(new BigDecimal("456.00"), someItem);
        someItem.getBids().add(bid2);
        em.persist(bid2);

        assertThat(someItem.getBids().size(), is(3)); // here 3
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Item item = em.find(Item.class, 1L);
        List<Bid> bids =  item.getBids();
        assertThat(bids.size(), is(3)); // here 2 and one null
        assertThat(bids.get(0), is(nullValue()));
        assertThat(bids.get(1).getAmount().compareTo(new BigDecimal("124")), is(0));
        assertThat(bids.get(2).getAmount().compareTo(new BigDecimal("456")), is(0));
    }

}

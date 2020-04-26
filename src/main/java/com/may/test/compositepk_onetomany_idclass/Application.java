package com.may.test.compositepk_onetomany_idclass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        Item item = new Item();
        item.setName("itemName");
        item.setItemId(1L);
        item.setCountry("USA");

        em.persist(item);

        Bid bid = new Bid();
        bid.setName("bidName");
        bid.setItem(item); // link
        item.getBids().add(bid); // link

        em.persist(bid);
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Item item = em.find(Item.class, new ItemId(1L, "USA"));
        assertThat(item.getBids().size(), is(1));
        assertThat(item.getBids().iterator().next().getName(), is("bidName"));
        assertThat(item.getBids().iterator().next().getBidItemId(), is(item.getItemId()));
        assertThat(item.getBids().iterator().next().getBidCountry(), is(item.getCountry()));
    }

}

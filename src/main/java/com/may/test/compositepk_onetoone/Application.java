package com.may.test.compositepk_onetoone;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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
        SharedPK id = new SharedPK(1L, "USA");
        item.setId(id);

        em.persist(item);

        Bid bid = new Bid();
        bid.setId(id);
        bid.setName("bidName");
        bid.setItem(item); // link

        em.persist(bid);
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Bid bid = em.find(Bid.class, new SharedPK(1L, "USA"));
        assertThat(bid.getItem(), is(not(nullValue())));
        assertThat(bid.getItem().getId(), is(not(nullValue())));
        assertThat(bid.getItem().getId(), is(bid.getId()));
        assertThat(bid.getItem().getId().getCountry(), is("USA"));
        assertThat(bid.getItem().getName(), is("itemName"));
    }

}

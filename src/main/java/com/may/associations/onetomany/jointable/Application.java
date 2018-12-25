package com.may.associations.onetomany.jointable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
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
        Item someItem = new Item("Some Item");
        em.persist(someItem);
        Item otherItem = new Item("Other Item");
        em.persist(otherItem);

        User someUser = new User("johndoe");
        someUser.getBoughtItems().add(someItem); // Link
        someItem.setBuyer(someUser); // Link
        someUser.getBoughtItems().add(otherItem);
        otherItem.setBuyer(someUser);
        em.persist(someUser);

        Item unsoldItem = new Item("Unsold Item");
        em.persist(unsoldItem);
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Item someItem = em.find(Item.class, 1L);
        assertThat(someItem.getBuyer().getUsername(), is("johndoe"));
        assertThat(someItem.getBuyer().getBoughtItems(), hasItem(someItem));

        Item unsoldItem = em.find(Item.class, 2L);
        assertThat(unsoldItem.getBuyer(), is(nullValue()));
    }

}

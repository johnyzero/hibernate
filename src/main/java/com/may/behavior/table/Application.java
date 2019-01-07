package com.may.behavior.table;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;


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
        Item item = new Item("item1");
        em.persist(item); // NO INSERT (strategy = GenerationType.TABLE or GenerationType.SEQUENCE)

        item.setName("item2"); // affects only persistence context

        Item someItem = em.find(Item.class, 1L); // NO SELECT
        assertThat(someItem, is(notNullValue()));
        assertThat(someItem.getName(), is("item2"));

        Item someItem2 = em.find(Item.class, 2L); // SELECT (persistence context doesn't have Item with id equal to 2L)
        assertThat(someItem2, is(nullValue()));

        someItem.setName("item3"); // affects only persistence context

        someItem = (Item) em.createQuery("from Item i where i.id = 1").getSingleResult(); // INSERT UPDATE SELECT

        // values from persistence context have precedence over values from result set
        assertThat(someItem.getName(), is("item3"));
    } // NO QUERIES

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Item item = em.find(Item.class, 1L);
        assertThat(item.getName(), is("item3"));
    }

}

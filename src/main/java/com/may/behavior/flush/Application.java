package com.may.behavior.flush;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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
        Item item = new Item("item1");
        em.persist(item); // INSERT

        item.setName("item2"); // NO UPDATE

        em.flush(); // FLUSH and UPDATE

        assertThat(em.contains(item), is(true)); // instance is in persistent state if EntityManager#contains(e)returns true

        item.setName("name-added-after-flush");

        em.flush(); // UPDATE
        em.clear(); // CLEAR persistence context

        assertThat(em.contains(item), is(false)); // now item is in detached state

        item.setName("some-name"); // this change will not hit the database
    } // FLUSH empty persistence context by returning from method

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Item item = em.find(Item.class, 1L);
        assertThat(item.getName(), is("name-added-after-flush"));
    }

    @EventListener
    @Order(2)
    @Transactional
    public void execute3(ApplicationReadyEvent event) {
        Item item;
        for (int i = 0; i < 20; i++) {
            item = new Item();
            em.persist(item);

            if (i % 10 == 0) { // 5, same as the JDBC batch size
                em.flush(); // flush a batch of inserts
                em.clear(); // and release memory to avoid OutOfMemoryException
            }
        }
    }

    @EventListener
    @Order(3)
    @Transactional
    public void execute4(ApplicationReadyEvent event) {
        Item item = new Item();
        item.setName("some-name");
        em.persist(item);
        em.flush(); // need to explicitly flush in order to use id in next statement (not the case when strategy == GenerationType.IDENTITY)
        doSomeThingElse(item.getId());
    }

    private void doSomeThingElse(Long id) {
        assertThat(id, is(not(nullValue())));
    }

}

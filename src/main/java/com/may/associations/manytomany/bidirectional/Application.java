package com.may.associations.manytomany.bidirectional;

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
        Category category1 = new Category("category1");
        Category category2 = new Category("category2");

        Item item1 = new Item("item1");
        Item item2 = new Item("item2");

        category1.getItems().add(item1);
        item1.getCategories().add(category1);

        category1.getItems().add(item2);
        item2.getCategories().add(category1);

        category2.getItems().add(item1);
        item1.getCategories().add(category2);

        em.persist(category1);
        em.persist(category2);
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Category category1 = (Category) em.createQuery("from Category c where c.name = 'category1'").getResultList().get(0);
        Category category2 = (Category) em.createQuery("from Category c where c.name = 'category2'").getResultList().get(0);

        Item item1 = (Item) em.createQuery("from Item i where i.name = 'item1'").getResultList().get(0);
        Item item2 = (Item) em.createQuery("from Item i where i.name = 'item2'").getResultList().get(0);

        assertThat(category1.getItems().size(), is(2));
        assertThat(item1.getCategories().size(), is(2));

        assertThat(category2.getItems().size(), is(1));
        assertThat(item2.getCategories().size(), is(1));

        assertThat(category2.getItems().iterator().next(), is(item1));
        assertThat(item2.getCategories().iterator().next(), is(category1));
    }

}

package com.may.associations.manytomany.ternary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        Category category1 = new Category("category1");
        Category category2 = new Category("category2");
        em.persist(category1);
        em.persist(category2);

        Item item1 = new Item("item1");
        Item item2 = new Item("item2");
        em.persist(item1);
        em.persist(item2);

        User user = new User("johndoe");
        em.persist(user);

        CategorizedItem linkOne = new CategorizedItem(
            user, item1
        );
        category1.getCategorizedItems().add(linkOne);

        CategorizedItem linkTwo = new CategorizedItem(
            user, item2
        );
        category1.getCategorizedItems().add(linkTwo);

        CategorizedItem linkThree = new CategorizedItem(
            user, item1
        );
        category2.getCategorizedItems().add(linkThree);
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Category category1 = (Category) em.createQuery("from Category c where c.name = 'category1'").getResultList().get(0);
        Category category2 = (Category) em.createQuery("from Category c where c.name = 'category2'").getResultList().get(0);

        Item item1 = (Item) em.createQuery("from Item i where i.name = 'item1'").getResultList().get(0);

        User user = (User) em.createQuery("from User u where u.username = 'johndoe'").getResultList().get(0);

        assertThat(category1.getCategorizedItems().size(), is(2));

        assertThat(category2.getCategorizedItems().size(), is(1));

        assertThat(category2.getCategorizedItems().iterator().next().getItem(), is(item1));
        assertThat(category2.getCategorizedItems().iterator().next().getAddedBy(), is(user));
    }

    @EventListener
    @Order(2)
    @Transactional
    public void execute3(ApplicationReadyEvent event) {
        Item item1 = (Item) em.createQuery("from Item i where i.name = 'item1'").getResultList().get(0);

        List<Category> categoriesOfItem =
            em.createQuery(
                "select c from Category c " +
                    "join c.categorizedItems ci " +
                    "where ci.item = :itemParameter")
                .setParameter("itemParameter", item1)
                .getResultList();

        assertThat(categoriesOfItem.size(), is(2));
    }

}

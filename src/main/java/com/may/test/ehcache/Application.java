package com.may.test.ehcache;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Cache;
import org.hibernate.cache.internal.EnabledCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Import(EntityManagerConfiguration.class)
public class Application {

    @PersistenceContext
    private EntityManager em;

    public static void main(String[] args) {
        SpringApplication.run(com.may.test.ehcache.Application.class, args);
    }

    @EventListener
    @Order(0)
    @Transactional
    public void execute(ApplicationReadyEvent event) {
        Item item1 = new Item("Item1");
        em.persist(item1);

        Bid bid1 = new Bid(new BigDecimal("124.00"), item1);
        item1.getBids().add(bid1);
        em.persist(bid1);

        Bid bid2 = new Bid(new BigDecimal("456.00"), item1);
        item1.getBids().add(bid2);
        em.persist(bid2);
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        Cache cache = em.getEntityManagerFactory().getCache().unwrap(org.hibernate.Cache.class);
        assertThat(cache.containsQuery("item_query_cache"), is(false));
        List<Item> items = em.createNamedQuery("item_named_query", Item.class).getResultList();
        assertThat(cache.containsQuery("item_query_cache"), is(true));
    }

    @EventListener
    @Order(2)
    @Transactional
    public void execute3(ApplicationReadyEvent event) {
        Cache cache = em.getEntityManagerFactory().getCache().unwrap(org.hibernate.Cache.class);
        assertThat(cache.containsQuery("item_query_cache"), is(true));
    }
}

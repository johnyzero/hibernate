package com.may.associations.onetomany.orphanremoval;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceImpl {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void save(User obj) {
        em.persist(obj);
    }

    @Transactional
    public void save(Item obj) {
        em.persist(obj);
    }

    @Transactional
    public Item retrieve() {
        return em.find(Item.class, 1L);
    }

    @Transactional
    public void clear() {
        Item item = em.find(Item.class, 1L);
        User user = em.find(User.class, 1L);
        Bid bid = item.getBids().iterator().next();
        item.getBids().remove(bid);

        System.out.println("Size : " + user.getBids().size());


        // alternative way to delete a bid (if orphanRemoval is false)
        // bid.setItem(null); // works only when item property is nullable (optional = true)
    }

    @Transactional
    public void test() {
        Item item = em.find(Item.class, 1L);
        User user = em.find(User.class, 1L);
        System.out.println("Size : " + item.getBids().size());
        System.out.println("Size : " + user.getBids().size());
    }
}

package com.may.behavior.jointransactionoptimisticlock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Service {

    @PersistenceContext
    public EntityManager em;

    public Service() {
    }

    @Transactional
    public void create() {
        Item item = new Item();
        item.setName("item1");
        em.persist(item);
    }

    @Transactional
    public Item load(Object id) {
        return em.find(Item.class, id);
    }

    @Transactional
    public Item change(String name, long timeout) {
        Item item = (Item) em.createQuery("from Item i where i.id = 1L").getSingleResult();
        item.setName(name);

        while (timeout > 0) {
            System.out.println("emulating work while modifiying " + name); // emulates some work
            timeout--;
        }

        em.persist(item);
        return item;
    }
}

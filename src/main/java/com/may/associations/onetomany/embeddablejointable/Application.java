package com.may.associations.onetomany.embeddablejointable;

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
        User user = new User("johndoe");
        Address deliveryAddress = new Address("Some Street", "12345", "Some City");
        user.setShippingAddress(deliveryAddress);
        em.persist(user);

        Shipment firstShipment = new Shipment();
        deliveryAddress.getDeliveries().add(firstShipment);
        em.persist(firstShipment);

        Shipment secondShipment = new Shipment();
        deliveryAddress.getDeliveries().add(secondShipment);
        em.persist(secondShipment);
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) {
        User johndoe = em.find(User.class, 1L);
        assertThat(johndoe.getShippingAddress().getDeliveries().size(), is(2));
    }

}

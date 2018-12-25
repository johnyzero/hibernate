package com.may.associations.onetoone.jointable;

import java.util.List;

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
		Shipment shipment = new Shipment();
		em.persist(shipment);

		Item item = new Item("Item");
		em.persist(item);

		Shipment shipmentWithItem = new Shipment(item);
		em.persist(shipmentWithItem);
	}

	@EventListener
	@Order(1)
	@Transactional
	public void execute2(ApplicationReadyEvent event) {
		List shipments = em.createQuery("FROM Shipment").getResultList();
		List items = em.createQuery("FROM Item").getResultList();
		assertThat(shipments.size(), is(2));
		assertThat(items.size(), is(1));
	}
}

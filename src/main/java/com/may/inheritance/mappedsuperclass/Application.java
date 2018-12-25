package com.may.inheritance.mappedsuperclass;

import org.hibernate.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootApplication
public class Application {

	@PersistenceContext
	private EntityManager em;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener
	@Transactional
	public void execute(ApplicationReadyEvent event) {
		BillingDetails cc = new CreditCard();
		BillingDetails ba = new BankAccount();
		BillingDetails bd = new BillingDetails();

		cc.setOwner("owner1");
		ba.setOwner("owner2");
		bd.setOwner("owner3");

		em.persist(cc);
		em.persist(ba);
		// em.persist(bd); throws error with message BillingDetails is not mapped

		// NOTE: Querying for BillingDetails via JPA will throw exception with message BillingDetails is not mapped
		// We can query for BillingDetails only via Hibernate criteria.
		List<Object> result = em.unwrap(Session.class).createCriteria(BillingDetails.class).list();
	}

}

package com.may.inheritance.mappedsuperclass.onetomany;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

		User user = new User();
		user.setDefaultBilling(cc);

		cc.setOwner("owner1");
		ba.setOwner("owner2");

		em.persist(user);
	}

}

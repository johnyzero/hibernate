package com.may.inheritance.mixed;

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
		CreditCard cc = new CreditCard();
		BankAccount ba = new BankAccount();
		BillingDetails bd = new BillingDetails();

		cc.setOwner("owner1");
		cc.setCardNumber("1111-1111-1111-1111");
		cc.setExpMonth("02");
		cc.setExpYear("2018");

		ba.setOwner("owner2");
		ba.setAccount("account1");
		ba.setBankname("Deutche Bank");
		ba.setSwift("swift");

		bd.setOwner("owner3");

		em.persist(cc);
		em.persist(ba);
		em.persist(bd);

		User user = new User();
		user.setBillingDetails(cc);
		em.persist(user);

		List r1 = em.createQuery("select b from BillingDetails b", BillingDetails.class).getResultList();
		User r2 = em.createQuery("select u from User u", User.class).getSingleResult();
	}

}

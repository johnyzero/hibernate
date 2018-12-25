package com.may.associations.onetoone.foreignkey;

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
		User user = new User("John");
		Address address = new Address("Street", "Zipcode", "City");
		user.setShippingAddress(address);
		em.persist(user);
	}

	@EventListener
	@Order(1)
	@Transactional
	public void execute2(ApplicationReadyEvent event) {
		List results = em.createQuery("FROM User").getResultList();
		assertThat(results.size(), is(1));
	}

}

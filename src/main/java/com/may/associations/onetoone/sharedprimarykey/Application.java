package com.may.associations.onetoone.sharedprimarykey;

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
		Address address = new Address("Street 123", "12345", "City");
		em.persist(address);

		User user = new User(address.getId(), "johndoe");
		em.persist(user);
	}

	@EventListener
	@Order(1)
	@Transactional
	public void execute2(ApplicationReadyEvent event) {
		List results = em.createQuery("FROM com.may.associations.onetoone.sharedprimarykey.User").getResultList();
		assertThat(results.size(), is(1));
	}

}

package com.may.inheritance.embeddable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
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
		Dimensions dimensions = new Dimensions();
		dimensions.setName("dimensions");
		dimensions.setSymbol("dimensions");
		dimensions.setDepth(BigDecimal.ONE);
		dimensions.setWidth(BigDecimal.ONE);
		dimensions.setHeight(BigDecimal.ONE);

		Weight weight = new Weight();
		weight.setName("weight");
		weight.setSymbol("weight");
		weight.setValue(BigDecimal.TEN);

		Item item = new Item();
		item.setName("item");
		item.setDimensions(dimensions);
		item.setWeight(weight);

		em.persist(item);

		List r = em.createQuery("select i from Item i", Item.class).getResultList();
	}

}

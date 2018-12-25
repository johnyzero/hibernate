package com.may.associations.onetomany.orphanremoval;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

	@Autowired
	public ServiceImpl service;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener
	public void execute(ApplicationReadyEvent event) {

		User user = new User();
		service.save(user);
		Item item = new Item();

		Bid bid = new Bid();
		bid.setAmount(BigDecimal.TEN);
		bid.setBidder(user);
		bid.setItem(item);
		item.getBids().add(bid);

		service.save(item);

		service.clear();

		service.test();
	}

}

package com.may.test.beforetransactioncleanup;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.action.spi.AfterTransactionCompletionProcess;
import org.hibernate.action.spi.BeforeTransactionCompletionProcess;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @PersistenceContext
    private EntityManager em;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    @Order(0)
    @Transactional
    public void execute(ApplicationReadyEvent event) {

        em.unwrap(SessionImpl.class).getActionQueue().registerProcess(new BeforeTransactionCompletionProcess() {
            @Override
            public void doBeforeTransactionCompletion(SessionImplementor session) {
                logger.info("before transaction cleanup");
            }
        });

        em.unwrap(SessionImpl.class).getActionQueue().registerProcess(new AfterTransactionCompletionProcess() {
            @Override
            public void doAfterTransactionCompletion(boolean success, SharedSessionContractImplementor session) {
                logger.info("after transaction cleanup");
            }
        });

        Item someItem = new Item("Some Item");
        em.persist(someItem);

        Bid bid1 = new Bid(new BigDecimal("124.00"), someItem);
        someItem.getBids().add(bid1);
        em.persist(bid1);

        Bid bid2 = new Bid(new BigDecimal("456.00"), someItem);
        someItem.getBids().add(bid2);
        em.persist(bid2);
    }

}

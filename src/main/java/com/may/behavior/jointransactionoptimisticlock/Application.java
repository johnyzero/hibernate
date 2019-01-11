package com.may.behavior.jointransactionoptimisticlock;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SpringBootApplication
public class Application {

    @Autowired
    private Service service;

    private ThreadPoolExecutor exec = new ThreadPoolExecutor(3, 10, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    @Order(0)
//    @Transactional
    public void execute(ApplicationReadyEvent event) throws InterruptedException {
        service.create();

        Callable<Item> task1 = () -> service.change("item2", 20000); // OptimisticLockException
        Callable<Item> task2 = () -> service.change("item3", 10000); // OptimisticLockException
        Callable<Item> task3 = () -> service.change("item4", 5000); // first win

        List<Callable<Item>> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        List<Future<Item>> futures = exec.invokeAll(tasks); // invoke multiple tasks and wait for all of them to complete

        List<Object> results = new ArrayList<>();
        for (Future future : futures) {
            try {
                results.add(future.get());
            } catch (ExecutionException e) {
                results.add(e.getCause());
            }
        }

        assertThat(results.get(0), is(instanceOf(ObjectOptimisticLockingFailureException.class)));
        assertThat(results.get(1), is(instanceOf(ObjectOptimisticLockingFailureException.class)));
        assertThat(results.get(2), both(is(instanceOf(Item.class))).and(hasProperty("name", equalTo("item4"))));
    }

}

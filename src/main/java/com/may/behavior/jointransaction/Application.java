package com.may.behavior.jointransaction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

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
        service.persistItem();

        Callable<Item> task1 = () -> service.changeItemName("item2", 20000); // last win
        Callable<Item> task2 = () -> service.changeItemName("item3", 10000);
        Callable<Item> task3 = () -> service.changeItemName("item4", 5000);

        List<Callable<Item>> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        exec.invokeAll(tasks); // invoke multiple tasks and wait for all of them to complete
    }

    @EventListener
    @Order(1)
    @Transactional
    public void execute2(ApplicationReadyEvent event) throws InterruptedException {
        Item item = service.load(1L);
        assertThat(item.getName(), is("item2"));
    }

}

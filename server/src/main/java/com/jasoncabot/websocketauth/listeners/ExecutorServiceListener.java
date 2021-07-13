package com.jasoncabot.websocketauth.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Provides a single instance of the ExecutorService in the ServletContext
 */
@WebListener
public class ExecutorServiceListener implements ServletContextListener {
    public static final String KEY = "executor_service";
    private static final Logger log = Logger.getLogger(ExecutorServiceListener.class.getName());

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        log.info("Creating scheduled executor service");
        scheduler = Executors.newScheduledThreadPool(2);
        servletContextEvent.getServletContext().setAttribute(KEY, scheduler);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        log.info("Destroying scheduled executor service");
        servletContextEvent.getServletContext().removeAttribute(KEY);
        scheduler.shutdown();

        try {
            boolean success = scheduler.awaitTermination(30, TimeUnit.SECONDS);
            if (!success) {
                scheduler.shutdownNow();
            }
        } catch (final InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}

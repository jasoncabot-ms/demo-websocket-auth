package com.jasoncabot.websocketauth.endpoints;

import com.jasoncabot.websocketauth.listeners.ExecutorServiceListener;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/messages", configurator = MessagingConfigurator.class)
public class MessagingWebSocket {

    private static final Logger log = Logger.getLogger(MessagingWebSocket.class.getName());

    private ScheduledFuture<?> generator;

    @OnOpen
    public void onWebSocketOpen(final Session session) {
        log.info(String.format("WebSocket session with id %s open", session.getId()));

        // We have access to the user through the standard getUserPrincipal
        final String username = session.getUserPrincipal().getName();

        generator = createMessageGenerator(session, username);
    }

    @OnMessage
    public void onWebSocketMessage(final Session session, final String message) {
        log.log(Level.INFO, String.format("Session %s sent message %s", session.getId(), message));
    }

    @OnError
    public void onWebSocketError(final Session session, final Throwable throwable) {
        log.log(Level.INFO, String.format("Session %s encountered error", session.getId()), throwable);
    }

    @OnClose
    public void onWebSocketClose(final Session session, final CloseReason closeReason) {
        log.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
        generator.cancel(false);
    }

    private ScheduledFuture<?> createMessageGenerator(final Session session, final String username) {
        final ScheduledExecutorService service = (ScheduledExecutorService) session.getUserProperties().get(ExecutorServiceListener.KEY);
        final Random random = new Random();
        random.setSeed(username.hashCode()); // random numbers are generated based on the username
        return service.scheduleAtFixedRate(() -> {
            try {
                final String value = "generated random number of " + random.nextInt(100) + " for user " + username;
                session.getBasicRemote().sendText(value);
            } catch (final IOException e) {
                log.log(Level.WARNING, "Sending fixed schedule event failed", e);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
package com.jasoncabot.websocketauth.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides a single instance of the ShortLivedTokenGenerator in the ServletContext
 */
@WebListener
public class TokenCacheListener implements ServletContextListener {
    public static final String KEY = "token_cache";

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        final TokenCache cache = new TokenCache();
        servletContextEvent.getServletContext().setAttribute(KEY, cache);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().removeAttribute(KEY);
    }

    public static class TokenCache {
        private final Map<String, String> cache = new ConcurrentHashMap<>();

        public String generateSingleUseToken(final Principal userPrincipal) {
            // This can be computed and stored in Redis or similar Key-Value store
            // with an expiration. For now we are simply storing it in memory
            final String token = UUID.randomUUID().toString();
            cache.put(token, userPrincipal.getName());
            return token;
        }

        public Optional<String> consumeToken(final String token) {
            if (!cache.containsKey(token)) return Optional.empty();
            return Optional.ofNullable(cache.remove(token));
        }
    }
}

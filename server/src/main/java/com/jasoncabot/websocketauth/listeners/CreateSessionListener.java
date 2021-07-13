package com.jasoncabot.websocketauth.listeners;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Ensures that a HttpSession exists for SocketEndpoint requests
 */
@WebListener
public class CreateSessionListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(final ServletRequestEvent servletRequestEvent) {

    }

    @Override
    public void requestInitialized(final ServletRequestEvent servletRequestEvent) {
        // WebSocket connections will not initialise a HttpSession by default
        // This means there will be no Servlet context or access to dependencies
        // To work around this, we create a session explicitly for every request
        ((HttpServletRequest)servletRequestEvent.getServletRequest()).getSession();
    }
}

package com.jasoncabot.websocketauth.endpoints;

import com.jasoncabot.websocketauth.listeners.ExecutorServiceListener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class MessagingConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(final ServerEndpointConfig config, final HandshakeRequest request, final HandshakeResponse response) {
        super.modifyHandshake(config, request, response);

        // This is called when the initial WebSocket connection is being established
        // It is too late to abort the connection as it has already been accepted
        // and different (browser) client implementations will handle it being
        // aborted here differently

        // We instead handle aborting the connection as a Filter

        final HttpSession httpSession = ((HttpSession) request.getHttpSession());
        final ServletContext servletContext = httpSession.getServletContext();
        final Object executorService = servletContext.getAttribute(ExecutorServiceListener.KEY);

        // We transfer the injected dependencies into our WebSocket connection that
        // is de-coupled from the normal servlet context
        // Essentially transferring from getAttributes() to getUserProperties()
        config.getUserProperties().put(ExecutorServiceListener.KEY, executorService);
    }

}

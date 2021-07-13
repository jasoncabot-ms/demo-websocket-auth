package com.jasoncabot.websocketauth.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

/**
 * Adds a very simple String as the default UserPrincipal to all requests arriving
 * over both HTTP and WebSockets
 */
public class UserRequestWrapper extends HttpServletRequestWrapper {
    private final String username;

    public UserRequestWrapper(final String name, final HttpServletRequest request) {
        super(request);

        this.username = name;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> username;
    }
}
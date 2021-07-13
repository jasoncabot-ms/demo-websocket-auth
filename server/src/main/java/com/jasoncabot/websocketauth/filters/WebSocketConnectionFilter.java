package com.jasoncabot.websocketauth.filters;

import com.jasoncabot.websocketauth.listeners.TokenCacheListener;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/messages")
public class WebSocketConnectionFilter implements Filter {
    private static final Logger log = Logger.getLogger(WebSocketConnectionFilter.class.getName());

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        log.info("processing access token filter");
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String token = request.getParameter("token");

        // Ensure that we have a valid token in the wss URL during connection
        final ServletContext context = request.getServletContext();
        final TokenCacheListener.TokenCache shortLivedTokenCache = (TokenCacheListener.TokenCache) context.getAttribute(TokenCacheListener.KEY);

        final Optional<String> userPrincipal = shortLivedTokenCache.consumeToken(token);
        if (!userPrincipal.isPresent()) {
            log.info("Invalid token, forbidden");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "An access token is required to connect");
            return;
        }

        filterChain.doFilter(new UserRequestWrapper(userPrincipal.get(), request), servletResponse);
    }

    @Override
    public void destroy() {

    }
}

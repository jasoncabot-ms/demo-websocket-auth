package com.jasoncabot.websocketauth.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/connections")
public class HttpConnectionFilter implements Filter {
    private static final Logger log = Logger.getLogger(HttpConnectionFilter.class.getName());

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        log.info("processing Authorization: Bearer <token> filter");
        final HttpServletRequest request = (HttpServletRequest) servletRequest;

        final String bearerToken = request.getHeader("Authorization").replace("Bearer ", "");

        // TODO: here you could look up the user based on their bearer token

        filterChain.doFilter(new UserRequestWrapper(bearerToken, request), servletResponse);
    }

    @Override
    public void destroy() {

    }
}

package com.jasoncabot.websocketauth.filters;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
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
        log.info("finding username for connection");
        final String username = lookupUsername(bearerToken);

        filterChain.doFilter(new UserRequestWrapper(username, request), servletResponse);
    }

    public String lookupUsername(final String bearerToken) {
        final String[] chunks = bearerToken.split("\\.");
        if (chunks.length < 2) return "Unknown User";
        final Base64.Decoder decoder = Base64.getDecoder();
        final String payload = new String(decoder.decode(chunks[1]));
        JSONParser parser = new JSONParser();
        try {
            final JSONObject json = (JSONObject) parser.parse(payload);
            return (String)json.getOrDefault("name", "Unknown User");
        } catch (final ParseException e) {
            log.log(Level.WARNING, "unable to parse JWT token", e);
            return "Unknown User";
        }
    }

    @Override
    public void destroy() {

    }
}

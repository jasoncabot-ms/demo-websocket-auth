package com.jasoncabot.websocketauth.resources;

import com.jasoncabot.websocketauth.listeners.TokenCacheListener;

import javax.servlet.ServletContext;
import javax.ws.rs.core.*;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.net.URI;
import java.util.logging.Logger;

@Path("/connections")
public class SecuredServerEndpoint {
    private static final Logger log = Logger.getLogger(SecuredServerEndpoint.class.getName());

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response buildEndpoint(@Context UriInfo ui, @Context SecurityContext securityContext, @Context ServletContext servletContext) {
        log.info("generating endpoint for user with token " + securityContext.getUserPrincipal().getName());

        // Create an access token that's valid only once for this user
        final boolean isSecure = "https".equals(ui.getRequestUri().getScheme());
        final TokenCacheListener.TokenCache shortLivedTokenCache = (TokenCacheListener.TokenCache) servletContext.getAttribute(TokenCacheListener.KEY);
        final String token = shortLivedTokenCache.generateSingleUseToken(securityContext.getUserPrincipal());

        // Tell this client the specific URI they can use to open a secure WebSocket
        final URI endpoint = ui.getBaseUriBuilder()
            .scheme(isSecure ? "wss" : "ws") // this should always be wss in production
            .path("/messages")
            .queryParam("token", token)
            .build();

        // Return the URL in the Location header field
        return Response.status(Response.Status.CREATED)
            .location(endpoint)
            .build();
    }
 
}
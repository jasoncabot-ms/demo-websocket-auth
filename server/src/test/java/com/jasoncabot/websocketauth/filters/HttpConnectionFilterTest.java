package com.jasoncabot.websocketauth.filters;

import junit.framework.TestCase;

public class HttpConnectionFilterTest extends TestCase {

    public void testLookupUsername() {
        // Sample JWT from jwt.io
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        final String name = new HttpConnectionFilter().lookupUsername(token);

        assertEquals("John Doe", name);
    }
}
package org.springstack.rsf.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;

import org.junit.Test;
import org.springstack.rsf.RSFServer;

public class HttpPingTest {

    private HttpPing httpPing = new HttpPing();

    @Test
    public void testCheckHttpServerIsAlive() {

        assertTrue(httpPing.isAlive(new RSFServer("baidu.com")));
        assertTrue(httpPing.isAlive(new RSFServer("apache.com")));
        assertFalse(httpPing.isAlive(new RSFServer("aaaabbc.com:8889")));
    }

    @Test
    public void testCheckCodeIsAlive() {
        assertTrue(httpPing.isAlive(HttpURLConnection.HTTP_CREATED));
        assertFalse(httpPing.isAlive(HttpURLConnection.HTTP_NOT_FOUND));
    }
}

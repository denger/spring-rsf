package org.springstack.rsf.util;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springstack.rsf.RSFServer;


/**
 * 基于 HTPP 的 Ping 实现，用于对服务端口进行健康检查。. 
 * 
 * @author denger
 * 15 Apr, 2014
 *
 */
public class HttpPing {

    private static int DEFAULT_PING_TIMEOUT = 1000;

    /**
     * RSFServer health check.
     * 
     * @param server RSFServer Instance.
     */
    public boolean isAlive(RSFServer server) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(String.valueOf(server.getURI()))
                    .openConnection();
            con.setUseCaches(false);
            con.setReadTimeout(DEFAULT_PING_TIMEOUT);
            con.setRequestMethod("HEAD");
            return isAlive(con.getResponseCode());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAlive(int responseCode) {
        return (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_BAD_REQUEST);
    }

}

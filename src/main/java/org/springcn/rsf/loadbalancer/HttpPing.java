package org.springcn.rsf.loadbalancer;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springcn.rsf.RSFServer;


/**
 * 基于 HTPP 的 Ping 实现，用于对服务端口进行健康检查。. 
 * 
 * @author denger
 * 15 Apr, 2014
 *
 */
public class HttpPing {


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

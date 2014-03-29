package org.springcn.rsf;

import java.net.URI;


/**
 * RSF Server.
 * 
 * @author denger
 *
 */
public class RSFServer {

	private String server;
	private int weight;

	public RSFServer(){}
	
	public RSFServer(String server){
		this.server = server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public String getServer() {
		return server;
	}

	public URI getURI() {
		String uri = server;
		if (server != null && !server.startsWith("http://")) {
			uri = "http://" + server;
		}
		return URI.create(uri);
	}

	public int hashCode() {
		return server.hashCode();
	}

	public boolean equals(RSFServer rsfResource) {
		return server.equals(rsfResource.server);
	}
}

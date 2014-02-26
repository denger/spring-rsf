package org.springcn.rsf.conf;

import java.util.ArrayList;
import java.util.List;

import org.springcn.rsf.RSFServer;
import org.springcn.rsf.RSFServersProvider;


/**
 * 默认 RSF Servers 提供者, 用于在本地配置中指定服务列表. 
 * 
 * @author denger
 *
 */
public class DefaultRSFServersProvider extends RSFServersProvider {

	private List<RSFServer> RSFServers = new ArrayList<RSFServer>();

	public DefaultRSFServersProvider(List<String> serverList) {
		initServerList(serverList);
	}

	private void initServerList(List<String> serverList) {
		for (String server : serverList) {
			RSFServer rsfServer = new RSFServer(server);

			RSFServers.add(rsfServer);
		}
	}

	@Override
	public List<RSFServer> getServers() {
		return RSFServers;
	}
}

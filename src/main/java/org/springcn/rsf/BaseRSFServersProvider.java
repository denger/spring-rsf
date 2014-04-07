package org.springcn.rsf;

import java.util.ArrayList;
import java.util.List;


/**
 * 默认 RSF Servers 提供者, 用于在本地配置中指定服务列表.
 * 
 * @author denger
 * 
 */
public class BaseRSFServersProvider extends AbstractRSFServersProvider {

	private List<RSFServer> servers = new ArrayList<RSFServer>();

	public BaseRSFServersProvider(List<String> serverListConfigs) {
		initServerList(serverListConfigs);
	}

	private void initServerList(List<String> serverList) {
		for (String server : serverList) {
			RSFServer rsfServer = new RSFServer(server);

			servers.add(rsfServer);
		}
	}

	@Override
	public List<RSFServer> getServerList() {
		return servers;
	}
}

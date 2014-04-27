package org.springstack.rsf;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springstack.rsf.util.Pinger;
import org.springstack.rsf.util.ShutdownEnabledTimer;


/**
 * 默认 RSF Servers 提供者, 用于在本地配置中指定服务列表.
 * 
 * @author denger
 * 
 */
public class LocalRSFServersProvider extends BaseRSFServersProvider {

	private List<RSFServer> servers = new ArrayList<RSFServer>();

	protected Timer lbTimer;
    protected int pingIntervalSeconds = 10;

	public LocalRSFServersProvider(List<String> serverListConfigs) {
		initServerList(serverListConfigs);

		// run pinger
		setupPingTask(Pinger.createPingTask(servers));
	}

	private void initServerList(List<String> serverList) {
		for (String server : serverList) {
			RSFServer rsfServer = new RSFServer(server);
			rsfServer.setAlive(Pinger.isAlive(rsfServer));

			servers.add(rsfServer);
		}
	}

	protected void setupPingTask(TimerTask timerTask){
		if(lbTimer != null){
			lbTimer.cancel();
		}
		lbTimer = new ShutdownEnabledTimer("RSFLoadBalancer-PingTimer", true);
		lbTimer.schedule(timerTask, 0, pingIntervalSeconds * 1000);
	}

	@Override
	public List<RSFServer> getServerList() {
		return servers;
	}

	@Override
	public void markServerDown(RSFServer server) {
		super.markServerDown(server);
	}
	
}

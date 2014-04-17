package org.springcn.rsf;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springcn.rsf.loadbalancer.AbstractLoadBalancerRule;
import org.springcn.rsf.loadbalancer.HttpPing;
import org.springcn.rsf.loadbalancer.RoundRobinRule;
import org.springcn.rsf.util.ShutdownEnabledTimer;

/**
 * RSF Servers Provider. <br/>
 * 须覆盖 getServerList 提供相应的服务列表数据源。并对 Servers 进行可用性的检测，实现服务的自动摘除和添加。
 * 
 * @author denger
 */
public abstract class BaseRSFServersProvider implements RSFServersProvider{

    private static Logger logger = LoggerFactory.getLogger(BaseRSFServersProvider.class);

    private final static RoundRobinRule DEFAULT_RULE = new RoundRobinRule();

	protected AbstractLoadBalancerRule loadBalancerRule;

    protected Timer lbTimer;
    protected int pingIntervalSeconds = 10;
    protected int pingTimeoutMillisecond = 1000;

    protected HttpPing ping = new HttpPing();
    protected AtomicBoolean pingInProgress = new AtomicBoolean(false);

	/**
	 * 设置默认的 loadBalancer 访问规则实现.
	 * 
	 * @param loadBalancerRule
	 */
	public BaseRSFServersProvider() {
		this.loadBalancerRule = DEFAULT_RULE; // 默认基于轮询访问策略
		this.loadBalancerRule.setRSFServersProvider(this);
		setupPingTask();
	}

	/**
	 * 设置指定 load balancer 实现策略.
	 * 
	 * @param loadBalancerRule
	 */
	public void setLoadBalancerRule(AbstractLoadBalancerRule loadBalancerRule) {
		this.loadBalancerRule = loadBalancerRule;
		if (loadBalancerRule.getRSFServersProvider() != this) {
			loadBalancerRule.setRSFServersProvider(this);
		}
		setupPingTask();
	}

	public RSFServer chooseServer() {
		return this.loadBalancerRule.choose();
	}

	protected void setupPingTask(){
		if(lbTimer != null){
			lbTimer.cancel();
		}
		lbTimer = new ShutdownEnabledTimer("RSFLoadBalancer-PingTimer", true);
		lbTimer.schedule(new PingTask(), 0, pingIntervalSeconds * 1000);
	}

	@Override
	public void addServers(List<RSFServer> newServers) {
		// do something..
	}

	@Override
	public void markServerDown(RSFServer server) {
		// do something...
	}

	public abstract List<RSFServer> getServerList();

	class PingTask extends TimerTask{

		@Override
		public void run() {
			//Pinger ping = new Pinger();
            try {
                //ping.runPinger();
            } catch (Throwable t) {
                logger.error("Throwable caught while running extends for ", t);
            }
		}
		
	}
}

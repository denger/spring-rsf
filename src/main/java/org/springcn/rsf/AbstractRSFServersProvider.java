package org.springcn.rsf;

import java.util.List;

import org.springcn.rsf.loadbalancer.AbstractLoadBalancerRule;
import org.springcn.rsf.loadbalancer.RandomRule;

/**
 * 
 * @author denger
 */
public abstract class AbstractRSFServersProvider implements RSFServersProvider{

	private AbstractLoadBalancerRule loadBalancerRule;

	/**
	 * 设置默认的 loadBalancer 访问规则实现.
	 * 
	 * @param loadBalancerRule
	 */
	public AbstractRSFServersProvider() {
		this.loadBalancerRule = new RandomRule(); // 默认基于随机访问策略
		this.loadBalancerRule.setRSFServersProvider(this);
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
	}

	public RSFServer chooseServer() {
		return this.loadBalancerRule.choose();
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
}

package org.springstack.rsf.loadbalancer;

import org.springstack.rsf.RSFServer;
import org.springstack.rsf.RSFServersProvider;

/**
 * LB 规则实现定义，其规则实现则为一个负载均衡的策略; 如一般常见的包括有轮询或基于响应时间的策略实现。
 * 
 * @author denger
 *
 */
public abstract class AbstractLoadBalancerRule {

	private RSFServersProvider serverProvider;

	public void setRSFServersProvider(RSFServersProvider serversProvider){
		this.serverProvider = serversProvider;
	}

	public RSFServersProvider getRSFServersProvider(){
		return this.serverProvider;
	}

	/**
     * 从 lb.allServers 选择一个可用的 RSFServer。
     * 
     * @return choosen Server object. NULL is returned if none
     *  server is available 
     */
	public abstract RSFServer choose();
}

package org.springstack.rsf;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springstack.rsf.loadbalancer.AbstractLoadBalancerRule;
import org.springstack.rsf.loadbalancer.RoundRobinRule;

/**
 * RSF Servers Provider. <br/>
 * 须覆盖 getServerList 提供相应的服务列表数据源。并对 Servers 进行可用性的检测，实现服务的自动摘除和添加。
 * 
 * @author denger
 */
public abstract class BaseRSFServersProvider implements RSFServersProvider{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final static RoundRobinRule DEFAULT_RULE = new RoundRobinRule();

    protected AbstractLoadBalancerRule loadBalancerRule;

    /**
     * 设置默认的 loadBalancer 访问规则实现.
     * 
     * @param loadBalancerRule
     */
    public BaseRSFServersProvider() {
        this.loadBalancerRule = DEFAULT_RULE; // 默认基于轮询访问策略
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
        // overview do something...
    }

    public String toString() {
        StringBuilder objStr = new StringBuilder();
        objStr.append("{RSFProviderClass:name=").append(getClass().getName())
                .append(",current list of Servers=").append(getServerList())
                .append("}");
        return objStr.toString();
    }

    public abstract List<RSFServer> getServerList();
}

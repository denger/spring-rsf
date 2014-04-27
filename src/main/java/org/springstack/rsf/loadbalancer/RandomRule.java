package org.springstack.rsf.loadbalancer;

import java.util.List;
import java.util.Random;

import org.springstack.rsf.RSFServer;
import org.springstack.rsf.RSFServersProvider;

/**
 * 随机负载均衡策略实现. Non Thread-Safe.

 * 
 * @author denger
 * 
 */
public class RandomRule extends AbstractLoadBalancerRule {

    private Random random;

    public RandomRule() {
        random = new Random();
    }

    public RSFServer choose(RSFServersProvider provider){
        if(provider == null){
            return null;
        }
        RSFServer server = null;
        List<RSFServer> serverList = provider.getServerList();

        // FIXME 循环意义不大，应该增加服务可用状态判断，并设置重试获取次数
        while(server == null){
            if(Thread.interrupted()){
                return null;
            }
            if(serverList == null || serverList.size() == 0){
                /**
                 * No servers. 
                 */
                return null;
            }
            int index = random.nextInt(serverList.size());
            server = serverList.get(index);
            if(server == null || !server.isAlive()){
                continue;
            }
        }
        return server;
    }

    @Override
    public RSFServer choose() {
        return choose(getRSFServersProvider());
    }
}

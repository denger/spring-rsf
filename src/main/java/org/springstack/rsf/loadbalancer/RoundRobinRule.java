package org.springstack.rsf.loadbalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springstack.rsf.RSFServer;
import org.springstack.rsf.RSFServersProvider;

/**
 * 轮询策略实现。Non Thread-Safe.
 * 
 * @author denger
 *
 */
public class RoundRobinRule extends AbstractLoadBalancerRule{
    
    private static Logger log = LoggerFactory.getLogger(RoundRobinRule.class);

    private AtomicInteger nextIndex;

    public RoundRobinRule(){
        this.nextIndex = new AtomicInteger(0);
    }

    public RSFServer choose(RSFServersProvider provider){
        int index = 0, count = 0;
        int tries = 10;
        RSFServer server = null;
        List<RSFServer> servers = provider.getServerList();

        while (server == null && count++ < tries){
            if(servers == null || servers.size() == 0){
                /**
                 * No servers. 
                 */
                return null;
            }
            index = nextIndex.incrementAndGet() % servers.size();
            server = servers.get(index);
            if(server == null || !server.isAlive()){
                // Next retry.
                server = null;
            }
        }
        if(count >= tries){
            log.warn("No available alive servers after {} tries from server list: {} ",
                    tries, provider);
        }
        return server;
    }

    @Override
    public RSFServer choose() {
        return choose(getRSFServersProvider());
    }

}

package org.springstack.rsf.util;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springstack.rsf.RSFServer;

/**
 * 提供对 Server 健康状态检查的任务。通过 <code>Pinger.createPingTask</code> 来创建对 Server List
 * 的状态检查的 Task.
 * 
 * @author denger
 * 
 */
public class Pinger{

    private static Logger logger = LoggerFactory.getLogger(Pinger.class);

    private static HttpPing httpPing = new HttpPing();

    public static PingTask createPingTask(List<RSFServer> servers){
        Pinger pinger = new Pinger(servers);

        return pinger.createTask();
    }

    public static boolean isAlive(RSFServer server){
        return httpPing.isAlive(server);
    }

    private List<RSFServer> servers;
    protected AtomicBoolean pingInProgress = new AtomicBoolean(false);

    protected Pinger(List<RSFServer> servers){
        this.servers = servers;
    }

    private PingTask createTask() {
        return new PingTask(this);
    }

    protected boolean isPingInProgress() {
        return pingInProgress.get();
    }

    protected HttpPing getHttpPing() {
        return httpPing;
    }

    protected void runPinger(){
        if(servers == null || servers.size() <= 0){
            return;    // no servers!
        }
        for (RSFServer server : servers) {
            if (server == null) {
                continue;
            }
            boolean oldIsAlive = server.isAlive();
            boolean isAlive = getHttpPing().isAlive(server);

            server.setAlive(isAlive);

            if (isAlive != oldIsAlive && logger.isDebugEnabled()) {
                logger.debug("Server Status:  Server {} status changed to {}",
                        server.getId(), (isAlive ? "ALIVE" : "DEAD"));
            }
        }
    }

    class PingTask extends TimerTask{
        Pinger pinger;
        public PingTask(Pinger pinger){
            this.pinger = pinger;
        }

        @Override
        public void run() {
            try {
                if (isPingInProgress()) {
                    return; // Ping in process
                } else {
                    pingInProgress.set(true);
                }
                pinger.runPinger();
            } catch (Throwable t) {
                logger.error("Throwable caught while running the Pinger-"
                        + this, t);
            } finally {
                pingInProgress.set(false);
            }
        }
    }
}

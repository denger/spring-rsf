package org.springstack.rsf.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springstack.rsf.RSFServer;

public class PingerTest {

    @Test
    public void testCheckServerListAvaliable(){
        List<RSFServer> servers = new ArrayList<RSFServer>();

        RSFServer baidu = new RSFServer("baidu.com");
        servers.add(baidu);
        assertFalse(baidu.isAlive());

        RSFServer example = new RSFServer("example.com");
        servers.add(example);
        assertFalse(example.isAlive());

        RSFServer abs = new RSFServer("notfound.domain");
        servers.add(abs);
        assertFalse(abs.isAlive());

        new Pinger(servers).runPinger();

        assertTrue(baidu.isAlive());
        assertTrue(example.isAlive());
        assertFalse(abs.isAlive());
    }
}
    
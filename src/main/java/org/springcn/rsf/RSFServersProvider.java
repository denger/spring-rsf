package org.springcn.rsf;

import java.util.List;
import java.util.Random;

import org.springcn.rsf.conf.DefaultRSFServersProvider;



/**
 * RSFServersProvider class. 用于获取 RESTful Server 列表, 由相应的子类来实现列表获取来源.
 * 
 * @author denger
 */
public abstract class RSFServersProvider {

	/**
     * 随机种子
     */
    private static Random random = new Random(System.currentTimeMillis());

    /**
     * 根据服务器列表创建 Servers 提供者.
     * @param serverList
     * @return
     */
	public static RSFServersProvider createProviderByLocalList(List<String> serverList){
		return new DefaultRSFServersProvider(serverList);
	}

	/**
	 * 获取服务列表, 获取来源由相应的子类实现.
	 */
	public abstract List<RSFServer> getServers();

	// TODO 简单版本随机选择服务器
	public RSFServer applyServerByRandom() {
		List<RSFServer> servers = getServers();

		int size = servers.size();
		if (size <= 0) {
			throw new RuntimeException("Not found any server!");
		}
		int index = random.nextInt(size);
		return servers.get(index); 
	}
}

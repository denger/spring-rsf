package org.springstack.rsf;

import java.util.List;

/**
 * RSFServersProvider class. 用于获取 RESTful Server 列表, 由相应的实现来获取 Server List 来源。
 * 
 * @author denger
 */
public interface RSFServersProvider {

    /**
     * 获取所有 Servers 服务列表(包括不可用服务)。
     */
    public List<RSFServer> getServerList();

    /**
     * 选择并获取一个可用的服务。
     * 
     * @return 返回可用服务实例，无可用实现则返回 Null.
     */
    public RSFServer chooseServer();

    /**
     * 添加新的服务列表。
     * 
     * @param newServers 新服务列表
     */
    public void addServers(List<RSFServer> newServers);

    /**
     * 将指定 Server 从可用列表中移除。
     * 
     * @param server 服务实例
     */
    public void markServerDown(RSFServer server);
}

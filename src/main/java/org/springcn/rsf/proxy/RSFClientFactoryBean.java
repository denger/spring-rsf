package org.springcn.rsf.proxy;

import java.util.List;

import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springcn.rsf.RSFServersProvider;
import org.springcn.rsf.http.HttpClientBuilder;
import org.springcn.rsf.http.RetryHttpClientExecutor;
import org.springcn.rsf.jackson.JacksonContextResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * 
 * RESTful Client Factory Bean.
 * @author dengfeige
 *
 * @param <T>
 */
@SuppressWarnings("deprecation")
public class RSFClientFactoryBean<T> implements FactoryBean<T>, InitializingBean  {

	private int connectionTimeout;

	private int readTimeout;

	private int maxTotalConnections;

	private int defaultMaxConnectionsPerHost;

	//默认不重试
	private int retry = 0;

	private Class<T> serviceInterface;

	private List<String> serverList;

	private T client;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 设置带重试机制的 Executor
		ClientExecutor clientExecutor = new RetryHttpClientExecutor(
				new HttpClientBuilder()
						.setConnectionTimeout(connectionTimeout)
						.setReadTimeout(readTimeout)
						.setMaxTotalConnections(maxTotalConnections)
						.setDefaultMaxPerRoute(defaultMaxConnectionsPerHost)
						.build(), retry);

		ResteasyProviderFactory	resteasyProviderFactory = ResteasyProviderFactory.getInstance();
		resteasyProviderFactory.register(JacksonContextResolver.class);
		RegisterBuiltin.register(resteasyProviderFactory);

		// 支持路由的代理类
		client = RouteProxy.create(serviceInterface, createRSFServersProvider(),
				clientExecutor, resteasyProviderFactory);
	}

	/*
	 * 创建 RSF Server 列表提供者.
	 */
	private RSFServersProvider createRSFServersProvider() {
		// 本地配置文件中的 server list 配置
		if(serverList != null && serverList.size() > 0){
			return RSFServersProvider.createProviderByLocalList(serverList);
		}
		throw new RuntimeException("Can't init RSF Services, Not fond any server list!");
	}

	@Override
	public T getObject() throws Exception {
		return client;
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setDefaultMaxConnectionsPerHost(int defaultMaxConnectionsPerHost) {
		this.defaultMaxConnectionsPerHost = defaultMaxConnectionsPerHost;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setServerList(List<String> serverList) {
		this.serverList = serverList;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public void setServiceInterface(Class<T> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}
}

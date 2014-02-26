package org.springcn.rsf.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springcn.rsf.RSFServer;
import org.springcn.rsf.RSFServersProvider;


/**
 * Route Proxy Handler.
 * 
 * @author dengfeige
 *
 * @param <T>
 */
@SuppressWarnings("deprecation") 
public class RouteProxy<T> implements InvocationHandler{

	public static <T> T create(Class<T> clazz, RSFServersProvider config,
			ClientExecutor executor, ResteasyProviderFactory providerFactory) {

		@SuppressWarnings("unchecked")
		T bean = (T) Proxy.newProxyInstance(clazz.getClassLoader(),
				new Class[] { clazz }, new RouteProxy<T>(clazz, executor, config, providerFactory));
		return bean;
	}

	private Class<T> clazz;
	private ClientExecutor executor;
	private ResteasyProviderFactory providerFactory;

	private RSFServersProvider serverProvider;

	// 每 Server 对应一个 Service 实例
	private Map<String, T> cacheServices = new HashMap<String, T>();

	public RouteProxy(Class<T> clazz, ClientExecutor executor, RSFServersProvider serverProvider, 
			ResteasyProviderFactory providerFactory) {
		this.clazz = clazz;
		this.executor = executor;
		this.providerFactory = providerFactory;
		this.serverProvider = serverProvider;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RSFServer server = serverProvider.applyServerByRandom();
		T proxyService = cacheServices.get(server.getServer());
		if (proxyService == null) {
			synchronized (cacheServices) {
				// 如果对应 URI 的 Service 不存在, 则根据 RUI 创建 RESTEasy Proxy Service
				proxyService = ProxyFactory.create(clazz, server.getURI(), executor, providerFactory);
				cacheServices.put(server.getServer(), proxyService);
			}
		}
		return method.invoke(proxyService, args);
	}
}

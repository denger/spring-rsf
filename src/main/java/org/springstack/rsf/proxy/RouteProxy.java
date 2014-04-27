package org.springstack.rsf.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springstack.rsf.RSFServer;
import org.springstack.rsf.RSFServersProvider;


/**
 * Route Proxy Handler.
 * 
 * @author dengfeige
 *
 * @param <T>
 */
@SuppressWarnings("deprecation") 
public class RouteProxy<T> implements InvocationHandler{
    
    private static Logger logger = Logger.getLogger(RouteProxy.class);

    public static <T> T create(Class<T> clazz, RSFServersProvider config,
            ClientExecutor executor, ResteasyProviderFactory providerFactory, int retry) {

        @SuppressWarnings("unchecked")
        T bean = (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[] { clazz }, new RouteProxy<T>(clazz, executor, config, providerFactory, retry));
        return bean;
    }

    private Class<T> clazz;
    private ClientExecutor executor;
    private ResteasyProviderFactory providerFactory;

    private RSFServersProvider serverProvider;

    private int retries;

    // 每 Server 对应一个 Service 实例
    private Map<String, T> cacheServices = new HashMap<String, T>();

    public RouteProxy(Class<T> clazz, ClientExecutor executor, RSFServersProvider serverProvider, 
            ResteasyProviderFactory providerFactory, int retries) {
        this.clazz = clazz;
        this.executor = executor;
        this.providerFactory = providerFactory;
        this.serverProvider = serverProvider;
        this.retries = retries;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for(int i = 0; i < retries + 1; i++){
            try{
                return invokeProxyServiceMethod(proxy, method, args);

            }catch(Exception e){
                handleHttpRetryException(i, e);
            }
        }
        return null;
    }

    public Object invokeProxyServiceMethod(Object proxy, Method method, Object[] args) throws IllegalAccessException, 
        IllegalArgumentException, InvocationTargetException  {
        RSFServer server = serverProvider.chooseServer();
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

    private void handleHttpRetryException(int retryCount, Exception e) throws Exception {
        logger.error(String.format("Request Exception, Retry: %d/%d", retryCount, retries));

        // 重试已经完成, 还是无法访问就扔出异常
        if(retryCount >= retries){
            throw e;
        }
    }
}

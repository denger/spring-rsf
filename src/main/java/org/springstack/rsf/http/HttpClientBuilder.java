package org.springstack.rsf.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * HttpClientBuilder.
 * 
 * e.g:
 * <pre>
 * HttpClient client = 
 * 		new HttpClientBuilder()
 * 			.setMaxTotalConnections(200)
 * 			.setConnectionTimeout(2000)
 * 			.build();
 * </pre>
 * 
 * @author dengfeige
 *
 */
public class HttpClientBuilder {

	/**
	 * 最大连接数
	 */
	public final static int DEFAULT_MAX_TOTAL_CONNECTIONS = 200;

	/**
	 * 连接超时时间ms
	 */
	public final static int DEFAULT_CONNECT_TIMEOUT = 5000;

	/**
	 * 读取超时时间ms
	 */
	public final static int DEFAULT_READ_TIMEOUT = 5000;

	/**
	 * 每个路由最大连接数
	 */
	public final static int DEFAULT_MAX_ROUTE_CONNECTIONS = 100;

	/**
	 * 默认编码.
	 */
	public final static String DEFAULT_CONTENT_CHARSET = "UTF-8";

	private HttpParams httpParams;
	private PoolingClientConnectionManager connectionManager;

	public HttpClientBuilder(){
		httpParams = getDefaultBasicParams();
		connectionManager = getDefaultConnectionManager();
	}

	private HttpParams getDefaultBasicParams() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_READ_TIMEOUT);

		HttpProtocolParams.setContentCharset(httpParams, DEFAULT_CONTENT_CHARSET);
		return httpParams;
	}

	private PoolingClientConnectionManager getDefaultConnectionManager(){
		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();

		connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_ROUTE_CONNECTIONS);
		return connectionManager;
	}

	public HttpClientBuilder setMaxTotalConnections(int maxTotalConnections){
		if(maxTotalConnections <= 0){
			maxTotalConnections = DEFAULT_MAX_TOTAL_CONNECTIONS;
		}
		connectionManager.setMaxTotal(maxTotalConnections);
		return this;
	}

	public HttpClientBuilder setDefaultMaxPerRoute(int maxPerRoute){
		if(maxPerRoute <= 0){
			maxPerRoute = DEFAULT_MAX_ROUTE_CONNECTIONS;
		}
		connectionManager.setDefaultMaxPerRoute(maxPerRoute);
		return this;
	}

	public HttpClientBuilder setConnectionTimeout(int connectionTimeout){
		if(connectionTimeout <= 0){
			connectionTimeout = DEFAULT_CONNECT_TIMEOUT;
		}
		HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout);
		return this;
	}

	public HttpClientBuilder setReadTimeout(int readTimeout){
		if(readTimeout <= 0){
			readTimeout = DEFAULT_READ_TIMEOUT;
		}
		HttpConnectionParams.setSoTimeout(httpParams, readTimeout);
		return this;
	}

	public HttpClient build() {
		return new DefaultHttpClient(connectionManager, httpParams);
	}
}

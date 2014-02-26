package org.springcn.rsf.http;

import java.net.SocketTimeoutException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

/**
 * RetryHttpClientExecutor
 * 
 * @author dengfeige
 */
@SuppressWarnings("deprecation")
public class RetryHttpClientExecutor extends ApacheHttpClient4Executor{

	private static final Logger LOGGER = Logger.getLogger(RetryHttpClientExecutor.class);

	//重试次数
	private int retry = 0;

	public RetryHttpClientExecutor(HttpClient httpClient, int retry){
		super(httpClient);
		this.retry = retry;
	}

	@Override
	public ClientResponse<?> execute(ClientRequest request) throws Exception {
		ClientResponse<?> response = null;
		for (int i = 0; i < this.retry + 1; i++) {
			try {
				response = super.execute(request);
				if (isReponseSuccessful(response)) {
					return response;
				}
				// 请求失败
				LOGGER.error(String.format("Request %s fail, status %s, Retry %d/%d",
						request.getUri(), response.getStatus(), i, retry));

				// 请求失败且无异常重试时需要释放连接, 出现异常时会由 RestEasy 自动释放连接(Response 为空)
				response.releaseConnection();

			} catch (ConnectTimeoutException cte) {
				LOGGER.warn(String.format("Connect %s Timeout, Retry %d/%d",
						request.getUri(), i, retry), cte);
			} catch (SocketTimeoutException ste) {
				LOGGER.warn(String.format(
						"Read %s Response Timeout, Retry %d/%d",
						request.getUri(), i, retry), ste);
			} catch (Exception e) {
				LOGGER.error(String.format("Request %s exception, Retry %d/%d",
						request.getUri(), i, retry), e);
			}
		}
		return response;
	}

	protected boolean isReponseSuccessful(ClientResponse<?> response) {
		int code = 0;
		if (response != null) {
			code = response.getStatus();
		}
		return code >= 200 && code < 300;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public int getRetry() {
		return retry;
	}
}

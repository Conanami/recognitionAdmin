package com.common.util;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class AcceptHeaderHttpRequestInterceptor implements
		ClientHttpRequestInterceptor {
	private final MediaType headerValue;

	public AcceptHeaderHttpRequestInterceptor(MediaType headerValue) {
		this.headerValue = headerValue;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
		requestWrapper.getHeaders().setAccept(
				Collections.singletonList(headerValue));

		return execution.execute(requestWrapper, body);
	}

}

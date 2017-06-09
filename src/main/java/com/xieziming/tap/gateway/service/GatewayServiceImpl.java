/*
 * Author: Suny Xie
 * Email: inbox@xieziming.com
 * Copyright (c) 2017 xieziming.com All rights reserved.
 */

package com.xieziming.tap.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by Suny on 7/6/16.
 */
@Component
@Slf4j
public class GatewayServiceImpl implements GatewayService{
    @Autowired
    private PoolingHttpClientConnectionManager pcm;

    @Value("${tap-testcase-service-url}")
    private String testCaseServiceUrl;

    @Value("${tap-execution-service-url}")
    private String executionServiceUrl;

    public static final String TEST_CASE_PATTERN = "/tap-testcase/";
    public static final String EXECUTION_PATTERN = "/tap-execution/";

    @Autowired
    public GatewayServiceImpl(PoolingHttpClientConnectionManager pcm){
        this.pcm = pcm;
    }

    private String getRouteredUrl(HttpServletRequest req) {
        String queryString = req.getQueryString() == null ? "" : "?" + req.getQueryString();

        String requestURI = req.getRequestURI();

        String url = null;
        if (requestURI.contains(TEST_CASE_PATTERN)) {
            url = testCaseServiceUrl + requestURI.replaceAll(TEST_CASE_PATTERN, "/") + queryString;
        } else if (requestURI.contains(EXECUTION_PATTERN)){
            url = executionServiceUrl + requestURI.replaceAll(EXECUTION_PATTERN, "/") + queryString;
        }
        return url;
    }

    @Override
    public HttpResponse getResponse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("got request for " + req.getRequestURL());
        String url = getRouteredUrl(req);

        HttpRequestBase httpRequest = getImplBaseMethod(req.getMethod(), url);

        if(httpRequest instanceof HttpPost){
            ((HttpPost) httpRequest).setEntity(new InputStreamEntity(req.getInputStream()));
        }

        addHeaders(httpRequest, req);
        log.info("forward request to "+ url);

        try(CloseableHttpClient httpClient = buildClient()){
            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            if(httpResponse.getEntity().getContentType() != null){
                resp.setContentType(httpResponse.getEntity().getContentType().getValue());
            }
            log.info("response from "+httpResponse);
            resp.setStatus(httpResponse.getStatusLine().getStatusCode());
            return httpResponse;
        }catch (Exception e) {
            log.error("Failed to build http client", e);
            return null;
        }
    }

    private void addHeaders(HttpRequestBase httpRequestBase, HttpServletRequest httpServletRequest){
        Enumeration<String> headerNameEnum = httpServletRequest.getHeaderNames();
        while(headerNameEnum.hasMoreElements()){
            String headerName = headerNameEnum.nextElement();
            if(!"Content-Length".equalsIgnoreCase(headerName)){
                httpRequestBase.setHeader(headerName, httpServletRequest.getHeader(headerName));
            }
        }
    }

    private HttpRequestBase getImplBaseMethod(String method, String url){
        return method.equals("GET")
                ? new HttpGet(url) : method.equals("OPTIONS")
                ? new HttpOptions(url) : method.equals("PUT")
                ? new HttpPut(url) : method.equals("DELETE")
                ? new HttpDelete(url) : new HttpPost(url);
    }

    private CloseableHttpClient buildClient(){
        return HttpClientBuilder.create()
                .setConnectionManager(pcm)
                .setConnectionManagerShared(true)
                .build();
    }
}

package com.dev.lsy.infrenspringbatchstudy.service;


import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiInfo;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiRequestVo;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiResponseVo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

public abstract class AbstractApiService {

    public ApiResponseVo service(List<? extends ApiRequestVo> apiRequest) {

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.errorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        }).build();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ApiInfo apiInfo = ApiInfo.builder().apiRequestList(apiRequest).build();

        return doApiService(restTemplate, apiInfo);

    }


    protected abstract ApiResponseVo doApiService(RestTemplate restTemplate, ApiInfo apiInfo);
}

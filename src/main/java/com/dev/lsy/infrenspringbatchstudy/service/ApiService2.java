package com.dev.lsy.infrenspringbatchstudy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class ApiService2 extends AbstractApiService{

    @Override
    protected ApiResponseVo doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8082/api/product/2", apiInfo, String.class);
        int statusCode = responseEntity.getStatusCodeValue();
        
        return ApiResponseVo.builder().status(statusCode).msg(responseEntity.getBody()).build();

    }
}

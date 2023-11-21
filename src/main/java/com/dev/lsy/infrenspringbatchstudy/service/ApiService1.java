package com.dev.lsy.infrenspringbatchstudy.service;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiInfo;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiResponseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class ApiService1 extends AbstractApiService{


    @Override
    protected ApiResponseVo doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8081/api/product/1", apiInfo, String.class);
        int statusCode = responseEntity.getStatusCodeValue();

        return ApiResponseVo.builder().status(statusCode).msg(responseEntity.getBody()).build();

    }
}

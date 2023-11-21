package com.dev.lsy.infrenspringbatchstudy.batch.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiInfo {

    private String url;
    private List<? extends ApiRequestVo> apiRequestList;
}

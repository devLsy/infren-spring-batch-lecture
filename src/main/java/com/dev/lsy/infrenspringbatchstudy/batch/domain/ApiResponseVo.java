package com.dev.lsy.infrenspringbatchstudy.batch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseVo {

    private int status;
    private String msg;
}

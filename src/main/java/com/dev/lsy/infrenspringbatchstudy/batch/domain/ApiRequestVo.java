package com.dev.lsy.infrenspringbatchstudy.batch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestVo {

    private Long id;
    private ProductVo productVo;
}

package com.dev.lsy.infrenspringbatchstudy.batch.chunk.processor;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiRequestVo;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductVo;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor2 implements ItemProcessor<ProductVo, ApiRequestVo> {
    
    @Override
    public ApiRequestVo process(ProductVo item) throws Exception {
        return ApiRequestVo.builder()
                .id(item.getId())
                .productVo(item)
                .build();
    }

}

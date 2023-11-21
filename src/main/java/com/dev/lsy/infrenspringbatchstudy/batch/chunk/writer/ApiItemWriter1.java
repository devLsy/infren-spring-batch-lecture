package com.dev.lsy.infrenspringbatchstudy.batch.chunk.writer;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiRequestVo;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ApiItemWriter1 implements ItemWriter<ApiRequestVo> {
    @Override
    public void write(List<? extends ApiRequestVo> items) throws Exception {

    }
}

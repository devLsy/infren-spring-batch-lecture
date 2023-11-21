package com.dev.lsy.infrenspringbatchstudy.batch.chunk.writer;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiRequestVo;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiResponseVo;
import com.dev.lsy.infrenspringbatchstudy.service.AbstractApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ApiItemWriter2 extends FlatFileItemWriter<ApiRequestVo> {

    private final AbstractApiService apiService;
    @Override
    public void write(List<? extends ApiRequestVo> items) throws Exception {
        ApiResponseVo responseVo = apiService.service(items);
        log.info("responseVo ==> [{}]", responseVo);

        items.forEach(item -> item.setApiResponseVo(responseVo));

        super.setResource(new FileSystemResource("output/product2.txt"));
        super.open(new ExecutionContext());
        super.setLineAggregator(new DelimitedLineAggregator<>());
        super.setAppendAllowed(true);
        super.write(items);
    }
}

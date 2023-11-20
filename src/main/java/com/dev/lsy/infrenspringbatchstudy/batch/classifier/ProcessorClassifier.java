package com.dev.lsy.infrenspringbatchstudy.batch.classifier;


import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiRequestVo;
import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductVo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C, T> implements Classifier {

    private Map<String, ItemProcessor<ProductVo, ApiRequestVo>> processorMap = new HashMap<>();

    @Override
    public Object classify(Object classifiable) {
        return (T)processorMap.get(((ProductVo) classifiable).getType());
    }

    public void setProcessorMap(Map<String, ItemProcessor<ProductVo, ApiRequestVo>> processorMap) {
        this.processorMap = processorMap;
    }
}

package com.dev.lsy.infrenspringbatchstudy.batch.classifier;


import com.dev.lsy.infrenspringbatchstudy.batch.domain.ApiRequestVo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class WriterClassifier<C, T> implements Classifier {

    private Map<String, ItemWriter<ApiRequestVo>> writerMap = new HashMap<>();

    @Override
    public Object classify(Object classifiable) {
        return (T)writerMap.get(((ApiRequestVo) classifiable).getProductVo().getType());
    }

    public void setWriterMap(Map<String, ItemWriter<ApiRequestVo>> writerMap) {
        this.writerMap = writerMap;
    }
}

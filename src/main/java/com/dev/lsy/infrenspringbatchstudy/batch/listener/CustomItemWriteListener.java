package com.dev.lsy.infrenspringbatchstudy.batch.listener;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

@Slf4j
public class CustomItemWriteListener implements ItemWriteListener<Product> {

    @Override
    public void beforeWrite(List<? extends Product> items) {

    }

    @Override
    public void afterWrite(List<? extends Product> items) {
        log.info("Thread : " + Thread.currentThread().getName() + ", write item : " + items.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Product> items) {

    }
}

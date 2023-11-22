package com.dev.lsy.infrenspringbatchstudy.batch.listener;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

@Slf4j
public class CustomItemReadListener implements ItemReadListener<Product> {

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(Product item) {
        log.info("Thread : " + Thread.currentThread().getName() + ", read item : " + item.getId());
    }

    @Override
    public void onReadError(Exception ex) {

    }
}

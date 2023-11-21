package com.dev.lsy.infrenspringbatchstudy.batch.listener;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

@Slf4j
public class CustomItemWriterListener implements ItemWriteListener<Customer> {
    @Override
    public void beforeWrite(List<? extends Customer> items) {

    }

    @Override
    public void afterWrite(List<? extends Customer> items) {
        log.info("Thread : " + Thread.currentThread().getName() + " write item : " + items.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Customer> items) {

    }
}

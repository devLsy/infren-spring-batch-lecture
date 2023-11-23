package com.dev.lsy.batchservice.batch.listener;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

@Slf4j
public class CustomItemReadListener implements ItemReadListener<Customer> {

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(Customer item) {
        log.info("Thread : " + Thread.currentThread().getName() + " read item : " + item.getId());
    }

    @Override
    public void onReadError(Exception ex) {

    }
}

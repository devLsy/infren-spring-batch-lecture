package com.dev.lsy.batchservice.batch.listener;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class CustomItermProcessorListener implements ItemProcessListener<Customer, Customer> {
    @Override
    public void beforeProcess(Customer item) {

    }

    @Override
    public void afterProcess(Customer item, Customer result) {
        log.info("Thread : " + Thread.currentThread().getName() + " process item : " + item.getId());
    }

    @Override
    public void onProcessError(Customer item, Exception e) {

    }
}

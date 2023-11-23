package com.dev.lsy.batchservice.batch.repository;

import com.dev.lsy.batchservice.batch.domain.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
}

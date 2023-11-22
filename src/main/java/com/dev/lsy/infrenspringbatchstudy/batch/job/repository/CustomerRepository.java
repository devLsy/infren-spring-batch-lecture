package com.dev.lsy.infrenspringbatchstudy.batch.job.repository;

import com.dev.lsy.infrenspringbatchstudy.batch.job.domain.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
}

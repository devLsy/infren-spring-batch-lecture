package com.dev.lsy.infrenspringbatchstudy.batch.repository;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
}

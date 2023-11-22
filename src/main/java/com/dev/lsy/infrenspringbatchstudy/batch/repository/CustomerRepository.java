package com.dev.lsy.infrenspringbatchstudy.batch.repository;

import com.dev.lsy.infrenspringbatchstudy.batch.job.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}

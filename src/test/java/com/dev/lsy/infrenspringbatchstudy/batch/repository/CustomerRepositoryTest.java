package com.dev.lsy.infrenspringbatchstudy.batch.repository;

import com.dev.lsy.infrenspringbatchstudy.batch.job.domain.CustomerEntity;
import com.dev.lsy.infrenspringbatchstudy.batch.job.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Commit
    @DisplayName("사용자저장")
    public void save() {

        for (int i = 1; i < 51; i++) {
            customerRepository.save(new CustomerEntity((long) i, i + "_firstName", i + "_lastName", i + "_birthdate"));
        }   
    }
}
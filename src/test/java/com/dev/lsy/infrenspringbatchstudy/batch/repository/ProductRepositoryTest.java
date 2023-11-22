package com.dev.lsy.infrenspringbatchstudy.batch.repository;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Commit
    public void save() {
        for (int i = 1; i < 100001; i++) {
            productRepository.save(new ProductEntity((long) i, i + "name", i + 1000, i + "type"));
        }
    }

}
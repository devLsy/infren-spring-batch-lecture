package com.dev.lsy.infrenspringbatchstudy.batch.repository;

import com.dev.lsy.infrenspringbatchstudy.batch.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}

package com.dev.lsy.infrenspringbatchstudy.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product")
public class ProductEntity {

    @Id
    private Long id;
    private String name;
    private int price;
    private String type;
}

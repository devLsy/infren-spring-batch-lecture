package com.dev.lsy.infrenspringbatchstudy.batch.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Product {
    
    @Id
    private Long id;
    private String name;
    private int price;
    private String type;
}

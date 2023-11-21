package com.dev.lsy.infrenspringbatchstudy.batch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthdate;
}

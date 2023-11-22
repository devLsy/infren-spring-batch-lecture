package com.dev.lsy.infrenspringbatchstudy.batch.job.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthdate;
}

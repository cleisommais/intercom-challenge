package com.cleison.itercom.challenge.domains;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class Customer {
    private String name;
    private Integer userId;
    private Double latitude;
    private Double longitude;
}

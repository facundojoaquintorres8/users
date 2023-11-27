package com.ar.users.dto;

import lombok.Data;

@Data
public class PhoneDTO {
    
    private Long number;
    private Integer cityCode;
    private String countryCode;

}

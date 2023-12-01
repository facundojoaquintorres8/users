package com.ar.users.dto;

import lombok.Data;

@Data
public class PhoneDTO {
    
    private Integer number;
    private Integer cityCode;
    private String countryCode;

}

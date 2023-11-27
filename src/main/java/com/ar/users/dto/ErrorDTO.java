package com.ar.users.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDTO {
    
    private Timestamp timestamp;
    private Integer code;
    private String detail;

}

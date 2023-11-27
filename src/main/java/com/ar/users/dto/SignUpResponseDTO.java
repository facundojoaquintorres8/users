package com.ar.users.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class SignUpResponseDTO {
    
    private UUID id;
    private Date created;
    private Date lastLogin;
    private String token;
    private Boolean isActive;

}

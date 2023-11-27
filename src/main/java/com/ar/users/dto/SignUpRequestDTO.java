package com.ar.users.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignUpRequestDTO {

    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private List<PhoneDTO> phones;

    
}

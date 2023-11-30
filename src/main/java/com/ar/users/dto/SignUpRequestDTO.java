package com.ar.users.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignUpRequestDTO {

    private String name;
    @NotBlank
    @NotBlank(message = "Email not entered")
    private String email;
    @NotBlank(message = "Password not entered")
    private String password;
    private List<PhoneDTO> phones;
    
}

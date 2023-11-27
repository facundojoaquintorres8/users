package com.ar.users.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserDTO {

    private UUID id;
    private Date created;
    private Date lastLogin;
    private String token;
    private Boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;

}

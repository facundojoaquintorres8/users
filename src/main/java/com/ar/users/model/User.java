package com.ar.users.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class User {

    @Id
    private UUID id;

    private Date created;

    private Date lastLogin;

    private String token;

    private Boolean isActive;

    private String name;

    @Column(unique = true)
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Phone> phones;

}

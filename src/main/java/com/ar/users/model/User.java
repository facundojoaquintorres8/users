package com.ar.users.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class User {

    @Id
    private UUID id;

    @NotNull
    private Date created;

    private Date lastLogin;

    @NotBlank
    private String token;

    @NotNull
    private Boolean isActive;

    private String name;

    @Column(unique = true)
    @NotBlank
    private String email;

    @Lob
    @Column(name="password", columnDefinition="bytea")
    private byte[] password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Phone> phones;

}

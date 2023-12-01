package com.ar.users.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Phone {

    @Id
    private UUID id;

    private Integer number;

    private Integer cityCode;

    private String countryCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

}

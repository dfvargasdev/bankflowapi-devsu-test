package com.devsu.bankflowapi.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDTO {

    private String name;
    private String address;
    private String phone;

    private String clientId;
    private String password;
    private Boolean status;

}

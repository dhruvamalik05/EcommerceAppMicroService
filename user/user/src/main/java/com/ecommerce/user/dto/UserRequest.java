package com.ecommerce.user.dto;

import com.ecommerce.user.model.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRequest {
    @JsonProperty("firstname")
    private String firstName;
    private String lastname;
    private String email;
    private String phone;
    private AddressDTO address;
}

package com.ecommerce.user.dto;

import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponse {
    private String id;
    @JsonProperty("firstname")
    private String firstName;
    private String lastname;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}

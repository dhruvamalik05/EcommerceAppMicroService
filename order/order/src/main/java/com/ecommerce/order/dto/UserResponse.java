package com.ecommerce.order.dto;


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

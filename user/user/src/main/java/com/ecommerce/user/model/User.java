package com.ecommerce.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity(name = "user_table")
@Document(collection = "user_table")
public class User {
    @Id
//    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private String id;
    @JsonProperty("firstname")
    private String firstName;
    private String lastname;

    @Indexed(unique = true)
    private String email;
    private String phone;
    private UserRole role = UserRole.CUSTOMER;

//    @OneToOne (cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn (name = "address_id", referencedColumnName = "id")
    private Address address;

//    @CreationTimestamp
    @CreatedDate
    private LocalDateTime createdAt;

//    @UpdateTimestamp
    @LastModifiedDate
    private LocalDateTime updatedAt;

}

package dev.asyncluna.rental.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    @NotBlank
    public String firstName;
    @NotBlank
    public String lastName;
    @Email
    @NotBlank
    @Column(unique = true)
    public String email;
    public String phone;

    protected Customer() {
    }

    public Customer(String f, String l, String e, String p) {
        firstName = f;
        lastName = l;
        email = e;
        phone = p;
    }
}

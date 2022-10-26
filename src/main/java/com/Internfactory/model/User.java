package com.Internfactory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;


@Entity
public class User {

@NotEmpty(message="First name is not in correct form")
    @Column(name = "firstname")
    private String Firstname;
    @Column(name = "lastname")
    private String Lastname;
    @Id@Email(message = "Email is not valid")
    @Column(name = "username")
    private String Email;
   @NotEmpty(message="Format of password is not correct")
   @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",message = "Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character:")
    @Column(name = "password")
    private String password;
    @Column(name = "Role")
   private String role;

    public User(String email, String password) {
        Email = email;
        this.password = password;
    }

    public User() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirst_name(String firstname) {
        Firstname = firstname;
    }

    public String getLast_name() {
        return Lastname;
    }

    public void setLast_name(String lastname) {
        Lastname = lastname;
    }

    public String getEmail() {
        return Email;
    }


    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

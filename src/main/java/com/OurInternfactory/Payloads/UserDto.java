package com.OurInternfactory.Payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Integer id;
    @NotEmpty
    @Size(min = 4, message = "First Name must be min of 4 character")
    private String firstname;
    private String lastname;
    @NotEmpty
    @Email(message = "Email Address is not Valid!!")
    private String email;
    @NotEmpty
    @Size(min = 8, message = "Password must be minimum of 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must Contain at least one uppercase letter, one lowercase letter, one numeric character, one special character and no spaces")
    private String password;
}

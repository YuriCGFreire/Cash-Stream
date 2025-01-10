package com.yuri.freire.Cash_Stream.Authentication.controllers.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Firstname cannot be null")
    @Size(min = 2, max = 50, message = "Firstname length must be between 2 and 50 characters")
    private String firstname;

    @NotEmpty(message = "Lastname cannot be null")
    @Size(min = 2, max = 50, message = "Lastname length must be between 2 and 50 characters")
    private String lastname;

    @NotEmpty(message = "Username cannot be null")
    @Size(min = 2, max = 50, message = "Username length must be between 2 and 50 characters")
    private String username;

    @NotEmpty(message = "Email cannot be null")
    @Email(message = "Your e-mail should be a valid email")
    private String email;

    @NotEmpty(message = "Password cannot be null")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "Password must meet the following criteria: \n" +
                    "- At least one uppercase letter (A-Z)\n" +
                    "- At least one lowercase letter (a-z)\n" +
                    "- At least one number (0-9)\n" +
                    "- At least one special character (e.g. #, $, %, etc.)\n" +
                    "- Minimum length of 8 characters"
    )
    private String password;

}

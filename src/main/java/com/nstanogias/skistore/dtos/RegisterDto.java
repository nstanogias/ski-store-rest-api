package com.nstanogias.skistore.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RegisterDto {

    @NotBlank(message = "displayName is required")
    private String displayName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
//    @Pattern(regexp="(?=^.{6,10}$)(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\\\s).*$",
//            message="{Password must have 1 Uppercase, 1 Lowercase, 1 number, 1 non alphanumeric and at least 6 characters}")
    private String password;
}

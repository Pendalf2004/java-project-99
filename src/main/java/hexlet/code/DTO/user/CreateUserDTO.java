package hexlet.code.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {

    private String  firstName;
    @NotBlank
    private String  lastName;
    @NotBlank
    @Email
    private String  email;
    @NotBlank
    private String  password;
}

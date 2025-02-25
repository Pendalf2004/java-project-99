package hexlet.code.app.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

    private String firstName;
    private String lastName;
    @Email
    @NotBlank
    private String email;
    private String password;
}

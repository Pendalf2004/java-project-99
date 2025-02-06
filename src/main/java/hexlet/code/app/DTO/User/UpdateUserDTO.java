package hexlet.code.app.DTO.User;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;
}

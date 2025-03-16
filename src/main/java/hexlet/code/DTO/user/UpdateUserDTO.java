package hexlet.code.DTO.user;

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

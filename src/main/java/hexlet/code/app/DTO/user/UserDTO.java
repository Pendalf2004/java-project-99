package hexlet.code.app.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {

    private Long        id;
    @Email
    @NotBlank
    private String      email;
    private String      firstName;
    private String      lastName;
    private LocalDate   createdAt;
}

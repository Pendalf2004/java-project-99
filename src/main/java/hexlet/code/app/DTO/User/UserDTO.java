package hexlet.code.app.DTO.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String password;
}

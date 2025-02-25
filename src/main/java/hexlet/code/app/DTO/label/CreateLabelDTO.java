package hexlet.code.app.DTO.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateLabelDTO {
    @NotBlank
    @Size(min = 3, max = 1000)
    private String name;
}

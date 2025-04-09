package hexlet.code.DTO.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateLabelDTO {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
}

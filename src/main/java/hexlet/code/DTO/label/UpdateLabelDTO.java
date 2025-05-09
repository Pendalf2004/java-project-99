package hexlet.code.DTO.label;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateLabelDTO {
    @Size(min = 3, max = 1000)
    private String name;
}

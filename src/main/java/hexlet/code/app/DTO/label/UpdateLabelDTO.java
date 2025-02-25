package hexlet.code.app.DTO.label;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class UpdateLabelDTO {
    @Size(min = 3, max = 1000)
    private String name;

}

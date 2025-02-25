package hexlet.code.app.DTO.taskStatus;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UpdateTaskStatusDTO {
    @Size(min = 1)
    private String name;

    @Size(min = 1)
    private String slug;
}

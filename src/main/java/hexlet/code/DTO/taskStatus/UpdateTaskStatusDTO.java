package hexlet.code.DTO.taskStatus;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateTaskStatusDTO {
    @Size(min = 1)
    private String name;

    @Size(min = 1)
    private String slug;
}

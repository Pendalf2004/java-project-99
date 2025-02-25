package hexlet.code.app.DTO.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.app.model.Label;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class CreateTaskDTO {
    @NotBlank
    @Size(min = 1)
    private String                  title;

    private Integer                 index;

    @NotNull
    private String                  content;

    @NotNull
    private String                  status;

    private Set<Long>              taskLabelIds;

//    @JsonProperty("assignee_id")
    private Long                    assigneeId;
}

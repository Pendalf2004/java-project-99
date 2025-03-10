package hexlet.code.app.DTO.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateTaskDTO {
    @NotBlank
    @Size(min = 1)
    private String    title;

    private Integer   index;

    private String    content;

    private String    status;

    private Set<Long> taskLabelIds;

//    @JsonProperty("assignee_id")
    private Long      assigneeId;
}

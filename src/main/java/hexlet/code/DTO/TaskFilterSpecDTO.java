package hexlet.code.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskFilterSpecDTO {
    private String  titleCont;
    private Long    assigneeId;
    private String  status;
    private Long    labelId;
}
